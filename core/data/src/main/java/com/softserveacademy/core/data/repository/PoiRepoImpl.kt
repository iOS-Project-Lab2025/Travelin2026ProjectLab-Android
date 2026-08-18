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
import com.softserveacademy.core.data.api.RouteMatrixElement
import android.util.Log
import com.google.android.libraries.places.api.model.PhotoMetadata
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

private const val TAG = "PoiRepoImpl"

/**
 * Implementation of [PoiRepo] that uses Google Places API and Google Maps Route Matrix API.
 * Provides data about points of interest, nearby transport, and restaurants.
 *
 * @property googleMapsApiService Service for interacting with Google Maps APIs.
 * @property placesClient Client for interacting with the Google Places SDK.
 * @property mapper Mapper for converting exceptions to [AppResult] failures.
 * @property context Application context used to retrieve the API key.
 */
@Singleton
class PoiRepoImpl @Inject constructor(
    private val googleMapsApiService: GoogleMapsApiService,
    private val placesClient: PlacesClient,
    private val mapper: ExceptionMapper,
    @param:ApplicationContext private val context: Context,
) : PoiRepo {

    private val apiKey: String by lazy {
        try {
            val ai = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            ai.metaData?.getString("com.google.android.geo.API_KEY") ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Fetches nearby tourist attractions and historical sites.
     *
     * @param latitude The latitude of the center point.
     * @param longitude The longitude of the center point.
     * @param radius Optional search radius in meters.
     * @return [AppResult] containing a list of [Poi] or an error.
     */
    override suspend fun getNearbyPlaces(
        latitude: Double,
        longitude: Double,
        radius: Double?
        requestNumber: Int
    ): AppResult<List<Poi>> = safeCall(mapper) {

        val fields = listOf(
            Place.Field.DISPLAY_NAME,
            Place.Field.TYPES,
            Place.Field.LOCATION,
            Place.Field.PHOTO_METADATAS,
            Place.Field.EDITORIAL_SUMMARY
        )
        val actualRadius = radius ?: searchRadius
        val types = listOf(
            "tourist_attraction",
            "museum",
            "park",
            "restaurant",
            "cafe",
            "shopping_mall",
            "movie_theater",
            "art_gallery",
            "aquarium"
        )
        val places = fetchPlaces(
            latitude,
            longitude,
            actualRadius,
            maxPoiSearch,
            types,
            fields
        )
        Log.d(TAG, "getNearbyPlaces: fetched ${places.size} places from Google SDK")
        places.forEach { Log.d(TAG, "Place found: ${it.displayName} at ${it.location}") }

        if (places.isEmpty()) return@safeCall emptyList()

        val routeMatrix = try {
            fetchRouteMatrix(latitude, longitude, places, "DRIVE")
        } catch (e: Exception) {
            Log.e(TAG, "Route Matrix failed, using backup distance calculation", e)
            emptyList()
        }
        mapPlacesToPois(latitude, longitude, places, routeMatrix, "drive")
    }

        val excludedTypes = listOf("condominium_complex", "lodging", "real_estate_agency")

        val places = fetchPlaces(
            latitude,
            longitude,
            5000.0,
            requestNumber,
            listOf("tourist_attraction", "museum", "park"),
            excludedTypes,
            fields
        )

        if (places.isEmpty()) return@safeCall emptyList()

        val routeMatrix = fetchRouteMatrix(latitude, longitude, places)

        mapPlacesToPois(places, routeMatrix, "walk")
    }

    /**
     * Fetches nearby transport hubs like bus and train stations.
     *
     * @param latitude The latitude of the center point.
     * @param longitude The longitude of the center point.
     * @return [AppResult] containing a list of [Poi] transport hubs.
     */
    override suspend fun getNearbyTransport(
        latitude: Double,
        longitude: Double,
        requestNumber: Int
    ): AppResult<List<Poi>> = safeCall(mapper) {

        val fields = listOf(
            Place.Field.DISPLAY_NAME,
            Place.Field.TYPES,
            Place.Field.LOCATION,
            Place.Field.PHOTO_METADATAS,
            Place.Field.EDITORIAL_SUMMARY
        )

        val types = listOf("bus_station", "train_station", "transit_station", "airport")

        val places = fetchPlaces(
            latitude,
            longitude,
            20000.0,
            requestNumber,
            types,
            emptyList(),
            fields
        )
        if (places.isEmpty()) return@safeCall emptyList()

        val routeMatrix = fetchRouteMatrix(latitude, longitude, places, "DRIVE")
        mapPlacesToPois(latitude, longitude, places, routeMatrix, "drive")

        mapPlacesToPois(places, routeMatrix, "drive")

    }

    /**
     * Fetches nearby restaurants, cafés, and bars.
     *
     * @param latitude The latitude of the center point.
     * @param longitude The longitude of the center point.
     * @return [AppResult] containing a list of restaurant [Poi]s.
     */
    override suspend fun getNearbyRestaurants(
        latitude: Double,
        longitude: Double,
        requestNumber: Int
    ): AppResult<List<Poi>> = safeCall(mapper) {
        val fields = listOf(
            Place.Field.DISPLAY_NAME,
            Place.Field.TYPES,
            Place.Field.LOCATION,
            Place.Field.PHOTO_METADATAS,
            Place.Field.EDITORIAL_SUMMARY
        )

        val places = fetchPlaces(
            latitude,
            longitude,
            5000.0,
            requestNumber,
            listOf("restaurant", "cafe"),
            listOf("hair_salon","hotel","night_club"),
            fields
        )
        
        if (places.isEmpty()) return@safeCall emptyList()

        val routeMatrix = fetchRouteMatrix(latitude, longitude, places)
        mapPlacesToPois(latitude, longitude, places, routeMatrix, "walk")

        mapPlacesToPois(places, routeMatrix, "walk")
    }

    /**
     * Fetch places using the Google Places SDK.
     *
     * @param lat Center latitude.
     * @param lng Center longitude.
     * @param radius Search radius in meters.
     * @param maxResults Maximum number of results to return.
     * @param includedTypes List of place types to include in the search.
     * @param excludedTypes List of place types to exclude from the search.
     * @param fields List of [Place.Field] to populate in the results.
     * @return A list of [Place] objects.
     */
    private suspend fun fetchPlaces(
        lat: Double,
        lng: Double,
        radius: Double,
        maxResults: Int,
        includedTypes: List<String>,
        excludedTypes: List<String> = emptyList(),
        fields: List<Place.Field>
    ): List<Place> {
        val circle = CircularBounds.newInstance(LatLng(lat, lng), radius)
        val request = SearchNearbyRequest.builder(circle, fields)
            .setIncludedTypes(includedTypes)
            .setExcludedTypes(excludedTypes)
            .setMaxResultCount(maxResults)
            .build()
        return placesClient.searchNearby(request).await().places
    }

    /**
     * Fetches travel duration and distance matrix from an origin to multiple destinations.
     *
     * @param lat Origin latitude.
     * @param lng Origin longitude.
     * @param destinations List of [Place] objects to calculate routes to.
     * @param travelMode The travel mode (e.g., "WALK", "DRIVE").
     * @return A list of [RouteMatrixElement] containing route info.
     */
    private suspend fun fetchRouteMatrix(
        lat: Double,
        lng: Double,
        destinations: List<Place>,
        travelMode: String = "WALK"
    ) = googleMapsApiService.computeRouteMatrix(
        apiKey = apiKey,
        fieldMask = "originIndex,destinationIndex,duration,distanceMeters,condition",
        request = RouteMatrixRequest(
            origins = listOf(RouteMatrixOrigin(Waypoint(LocationDto(LatLngDto(lat, lng))))),
            destinations = destinations.map { place ->
                RouteMatrixDestination(
                    Waypoint(
                        LocationDto(
                            LatLngDto(
                                place.location?.latitude ?: 0.0, place.location?.longitude ?: 0.0
                            )
                        )
                    )
                )
            },
            travelMode = travelMode
        )
    )

    /**
     * Maps a list of [Place] objects and their route data into domain [Poi] objects.
     *
     * @param originLat Latitude of the user/origin.
     * @param originLon Longitude of the user/origin.
     * @param places The list of places to map.
     * @param routeMatrix The corresponding route data.
     * @param suffix Suffix to append to the travel time (e.g., "walk").
     * @return A list of mapped [Poi] domain objects.
     */
    private suspend fun mapPlacesToPois(
        originLat: Double,
        originLon: Double,
        places: List<Place>,
        routeMatrix: List<RouteMatrixElement>,
        suffix: String
    ): List<Poi> = coroutineScope {
        places.mapIndexed { index, place ->
            async {
                val element = routeMatrix.find { it.destinationIndex == index }
                val itemLat = place.location?.latitude ?: 0.0
                val itemLon = place.location?.longitude ?: 0.0
                
                // Backup logic: If Route Matrix API fails, calculate straight-line distance
                val distance = element?.distanceMeters ?: calculateLocalDistance(originLat, originLon, itemLat, itemLon)
                // Estimate driving time (approx 10 m/s or 36 km/h) if duration is missing
                val duration = element?.duration ?: "${(distance / 10.0).toInt()}s"

                Poi(
                    name = place.displayName ?: "Unknown",
                    type = place.placeTypes?.firstOrNull()?.formatType() ?: "POI",
                    travelTime = "${formatDuration(duration)} $suffix",
                    distanceMeters = distance,
                    imageUrl = fetchPhotoUri(place.photoMetadatas?.firstOrNull()),
                    description = place.editorialSummary,
                    latitude = itemLat,
                    longitude = itemLon
                )
            }
        }.awaitAll()
    }

    private fun calculateLocalDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
        val r = 6371000 // Earth radius in meters
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return (r * c).toInt()
    }

    /**
     * Resolves the URI for a place's photo metadata.
     *
     * @param photoMetadata The metadata for the photo.
     * @return The resolved URI string or null if resolution fails.
     */
    private suspend fun fetchPhotoUri(
        photoMetadata: PhotoMetadata?
    ): String? {
        if (photoMetadata == null) {
            Log.d(TAG, "fetchPhotoUri: No photo metadata available for this place")
            return null
        }
        return try {
            val result = placesClient.fetchResolvedPhotoUri(
                FetchResolvedPhotoUriRequest.builder(photoMetadata).build()
            ).await()
            val uri = result.uri?.toString()
            Log.d(TAG, "fetchPhotoUri: Successfully resolved URI: $uri")
            uri
        } catch (e: Exception) {
            Log.e(TAG, "fetchPhotoUri: Failed to resolve photo URI: ${e.message}")
            null
        }
    }

    /**
     * Formats a duration string (e.g., "300s") into a user-friendly "X min" format.
     *
     * @param durationStr The raw duration string from the API.
     * @return A formatted duration string.
     */
    private fun formatDuration(
        durationStr: String?
    ): String {
        val seconds = durationStr?.removeSuffix("s")?.toDoubleOrNull() ?: 0.0
        val minutes = (seconds / 60).roundToInt()
        return if (minutes < 1) "1 min" else "$minutes min"
    }

    /**
     * Extension to format place types from API style (SNAKE_CASE) to Title case.
     */
    private fun String.formatType() = replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
}
