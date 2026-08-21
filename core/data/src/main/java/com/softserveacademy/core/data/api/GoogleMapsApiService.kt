package com.softserveacademy.core.data.api

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

@Serializable
data class LatLngDto(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class LocationDto(
    val latLng: LatLngDto
)

@Serializable
data class Waypoint(
    val location: LocationDto
)

@Serializable
data class RouteMatrixOrigin(
    val waypoint: Waypoint
)

@Serializable
data class RouteMatrixDestination(
    val waypoint: Waypoint
)

@Serializable
data class RouteMatrixRequest(
    val origins: List<RouteMatrixOrigin>,
    val destinations: List<RouteMatrixDestination>,
    val travelMode: String = "WALK",
    val routingPreference: String = "ROUTING_PREFERENCE_UNSPECIFIED"
)

@Serializable
data class RouteMatrixElement(
    val originIndex: Int? = null,
    val destinationIndex: Int? = null,
    val distanceMeters: Int? = null,
    val duration: String? = null,
    val condition: String? = null
)

interface GoogleMapsApiService {
    @POST("distanceMatrix/v2:computeRouteMatrix")
    suspend fun computeRouteMatrix(
        @Header("X-Goog-Api-Key") apiKey: String,
        @Header("X-Goog-FieldMask") fieldMask: String,
        @Header("X-Android-Package") packageName: String,
        @Body request: RouteMatrixRequest
    ): List<RouteMatrixElement>
}
