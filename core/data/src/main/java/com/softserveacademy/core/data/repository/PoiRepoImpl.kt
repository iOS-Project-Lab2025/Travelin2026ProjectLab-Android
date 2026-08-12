package com.softserveacademy.core.data.repository

import android.content.Context
import android.content.pm.PackageManager
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchResolvedPhotoUriRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import com.softserveacademy.core.data.api.GoogleMapsApiService
import com.softserveacademy.core.data.api.LatLngDto
import com.softserveacademy.core.data.api.LocationDto
import com.softserveacademy.core.data.api.RouteMatrixDestination
import com.softserveacademy.core.data.api.RouteMatrixOrigin
import com.softserveacademy.core.data.api.RouteMatrixRequest
import com.softserveacademy.core.data.api.Waypoint
import com.softserveacademy.core.domain.model.Poi
import com.softserveacademy.core.domain.repository.PoiRepo
import com.softserveacademy.core.error.mapper.ExceptionMapper
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.core.error.util.safeCall
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class PoiRepoImpl @Inject constructor(
    private val googleMapsApiService: GoogleMapsApiService,
    private val placesClient: PlacesClient,
    private val mapper: ExceptionMapper,
    @param:ApplicationContext private val context: Context,
) : PoiRepo {

    var searchRadius = 5000.0
    var maxPoiSearch = 10

    private val apiKey: String by lazy {
        val ai = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        ai.metaData.getString("com.google.android.geo.API_KEY") ?: ""
    }

    override suspend fun getNearbyPlaces(latitude: Double, longitude: Double): AppResult<List<Poi>> = safeCall(mapper) {
        val center = LatLng(latitude, longitude)
        val circle = CircularBounds.newInstance(center, searchRadius) // 5km radius
        val placeFields = listOf(
            Place.Field.DISPLAY_NAME,
            Place.Field.TYPES,
            Place.Field.LOCATION,
            Place.Field.PHOTO_METADATAS,
            Place.Field.EDITORIAL_SUMMARY
        )

        val request = SearchNearbyRequest.builder(circle, placeFields)
            .setIncludedTypes(listOf("tourist_attraction", "museum", "park"))
            .setMaxResultCount(maxPoiSearch)
            .build()

        val response = placesClient.searchNearby(request).await()
        val places = response.places

        if (places.isEmpty()) return@safeCall emptyList()

        val routeMatrixRequest = RouteMatrixRequest(
            origins = listOf(RouteMatrixOrigin(Waypoint(LocationDto(LatLngDto(latitude, longitude))))),
            destinations = places.map { place ->
                RouteMatrixDestination(
                    Waypoint(
                        LocationDto(
                            LatLngDto(
                                place.location?.latitude ?: 0.0,
                                place.location?.longitude ?: 0.0
                            )
                        )
                    )
                )
            }
        )

        val routeMatrixResponse = googleMapsApiService.computeRouteMatrix(
            apiKey = apiKey,
            fieldMask = "originIndex,destinationIndex,duration,distanceMeters,condition",
            request = routeMatrixRequest
        )

        coroutineScope {
            places.mapIndexed { index, place ->
                async {
                    val element = routeMatrixResponse.find { it.destinationIndex == index }

                    val walkingTime = element?.duration?.let { durationStr ->
                        val seconds = durationStr.removeSuffix("s").toDoubleOrNull() ?: 0.0
                        val minutes = (seconds / 60).roundToInt()
                        if (minutes < 1) "1 min walk" else "$minutes min walk"
                    } ?: "N/A"

                    val photoMetadata = place.photoMetadatas?.firstOrNull()
                    val imageUrl = photoMetadata?.let {
                        try {
                            val uriResponse = placesClient.fetchResolvedPhotoUri(
                                FetchResolvedPhotoUriRequest.builder(it).build()
                            ).await()
                            uriResponse.uri?.toString()
                        } catch (_: Exception) {
                            null
                        }
                    }

                    Poi(
                        name = place.displayName ?: "Unknown",
                        type = place.placeTypes?.firstOrNull()?.replace("_", " ")?.lowercase()
                            ?.replaceFirstChar { it.uppercase() } ?: "POI",
                        travelTime = walkingTime,
                        imageUrl = imageUrl,
                        description = place.editorialSummary
                    )
                }
            }.awaitAll()
        }
    }

    override suspend fun getAreaDescription(latitude: Double, longitude: Double): AppResult<String?> = safeCall(mapper) {
        val center = LatLng(latitude, longitude)
        val circle = CircularBounds.newInstance(center, 10000.0)

        val placeFields = listOf(Place.Field.EDITORIAL_SUMMARY, Place.Field.DISPLAY_NAME, Place.Field.TYPES)

        val request = SearchNearbyRequest.builder(circle, placeFields)
            .setIncludedTypes(listOf("tourist_attraction", "museum", "park", "art_gallery"))
            .setMaxResultCount(20)
            .build()

        val response = placesClient.searchNearby(request).await()

        response.places.firstOrNull { !it.editorialSummary.isNullOrEmpty() }?.editorialSummary
    }

    override suspend fun getNearbyTransport(latitude: Double, longitude: Double): AppResult<List<Poi>> = safeCall(mapper) {
        val center = LatLng(latitude, longitude)
        val circle = CircularBounds.newInstance(center, 20000.0)
        val placeFields = listOf(
            Place.Field.DISPLAY_NAME,
            Place.Field.TYPES,
            Place.Field.LOCATION
        )

        val request = SearchNearbyRequest.builder(circle, placeFields)
            .setIncludedTypes(listOf("bus_station", "train_station", "transit_station", "airport"))
            .setMaxResultCount(3)
            .build()

        val response = placesClient.searchNearby(request).await()
        val places = response.places

        if (places.isEmpty()) return@safeCall emptyList()

        val routeMatrixRequest = RouteMatrixRequest(
            origins = listOf(RouteMatrixOrigin(Waypoint(LocationDto(LatLngDto(latitude, longitude))))),
            destinations = places.map { place ->
                RouteMatrixDestination(
                    Waypoint(
                        LocationDto(
                            LatLngDto(
                                place.location?.latitude ?: 0.0,
                                place.location?.longitude ?: 0.0
                            )
                        )
                    )
                )
            },
            travelMode = "DRIVE"
        )

        val routeMatrixResponse = googleMapsApiService.computeRouteMatrix(
            apiKey = apiKey,
            fieldMask = "originIndex,destinationIndex,duration,distanceMeters,condition",
            request = routeMatrixRequest
        )

        places.mapIndexed { index, place ->
            val element = routeMatrixResponse.find { it.destinationIndex == index }
            val time = element?.duration?.let { durationStr ->
                val seconds = durationStr.removeSuffix("s").toDoubleOrNull() ?: 0.0
                val minutes = (seconds / 60).roundToInt()
                if (minutes < 1) "1 min" else "$minutes min"
            } ?: "N/A"

            val isTransit = place.placeTypes?.any { it.contains("station") || it.contains("transit") } == true
            val suffix = if (isTransit) "walk" else "drive"

            Poi(
                name = place.displayName ?: "Station",
                type = place.placeTypes?.firstOrNull() ?: "Transport",
                travelTime = "$time $suffix"
            )
        }
    }

    override suspend fun getNearbyRestaurants(latitude: Double, longitude: Double): AppResult<List<Poi>> = safeCall(mapper) {
        val center = LatLng(latitude, longitude)
        val circle = CircularBounds.newInstance(center, searchRadius)
        val placeFields = listOf(
            Place.Field.DISPLAY_NAME,
            Place.Field.TYPES,
            Place.Field.LOCATION,
            Place.Field.PHOTO_METADATAS
        )

        val request = SearchNearbyRequest.builder(circle, placeFields)
            .setIncludedTypes(listOf("restaurant", "cafe", "bar"))
            .setMaxResultCount(3)
            .build()

        val response = placesClient.searchNearby(request).await()
        val places = response.places

        if (places.isEmpty()) return@safeCall emptyList()

        val routeMatrixRequest = RouteMatrixRequest(
            origins = listOf(RouteMatrixOrigin(Waypoint(LocationDto(LatLngDto(latitude, longitude))))),
            destinations = places.map { place ->
                RouteMatrixDestination(
                    Waypoint(
                        LocationDto(
                            LatLngDto(
                                place.location?.latitude ?: 0.0,
                                place.location?.longitude ?: 0.0
                            )
                        )
                    )
                )
            }
        )

        val routeMatrixResponse = googleMapsApiService.computeRouteMatrix(
            apiKey = apiKey,
            fieldMask = "originIndex,destinationIndex,duration,distanceMeters,condition",
            request = routeMatrixRequest
        )

        coroutineScope {
            places.mapIndexed { index, place ->
                async {
                    val element = routeMatrixResponse.find { it.destinationIndex == index }

                    val walkingTime = element?.duration?.let { durationStr ->
                        val seconds = durationStr.removeSuffix("s").toDoubleOrNull() ?: 0.0
                        val minutes = (seconds / 60).roundToInt()
                        if (minutes < 1) "1 min walk" else "$minutes min walk"
                    } ?: "N/A"

                    val photoMetadata = place.photoMetadatas?.firstOrNull()
                    val imageUrl = photoMetadata?.let {
                        try {
                            val uriResponse = placesClient.fetchResolvedPhotoUri(
                                FetchResolvedPhotoUriRequest.builder(it).build()
                            ).await()
                            uriResponse.uri?.toString()
                        } catch (_: Exception) {
                            null
                        }
                    }

                    Poi(
                        name = place.displayName ?: "Unknown",
                        type = place.placeTypes?.firstOrNull()?.replace("_", " ")?.lowercase()
                            ?.replaceFirstChar { it.uppercase() } ?: "Restaurant",
                        travelTime = walkingTime,
                        imageUrl = imageUrl
                    )
                }
            }.awaitAll()
        }
    }
}
