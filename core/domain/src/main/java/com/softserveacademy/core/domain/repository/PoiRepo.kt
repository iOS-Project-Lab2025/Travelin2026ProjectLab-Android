package com.softserveacademy.core.domain.repository

import com.softserveacademy.core.domain.model.Poi
import com.softserveacademy.core.error.model.AppResult

/**
 * Interface for fetching nearby points of interest and calculating routes to them.
 */
interface PoiRepo {
    /**
     * Fetch nearby places of interest for a specific location.
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @return A list of nearby places with walking times.
     */
    suspend fun getNearbyPlaces(latitude: Double, longitude: Double): AppResult<List<Poi>>

    /**
     * Fetch a description of the area for a specific location.
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @return A string containing the area description.
     */
    suspend fun getAreaDescription(latitude: Double, longitude: Double): AppResult<String?>

    /**
     * Fetch nearby transport hubs for a specific location.
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @return A list of nearby transport hubs with walking/driving times.
     */
    suspend fun getNearbyTransport(latitude: Double, longitude: Double): AppResult<List<Poi>>

    /**
     * Fetch nearby restaurants for a specific location.
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @return A list of nearby restaurants with walking times.
     */
    suspend fun getNearbyRestaurants(latitude: Double, longitude: Double): AppResult<List<Poi>>
}
