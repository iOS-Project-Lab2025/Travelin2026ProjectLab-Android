package com.softserveacademy.feature.booking.flight.data.remote

import com.softserveacademy.core.domain.model.FlightOffer
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for Flight API endpoints.
 */
interface FlightApi {
    @GET("v1/flights/search")
    suspend fun searchFlights(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("adults") adults: Int,
        @Query("children") children: Int,
        @Query("infants") infants: Int
    ): List<FlightOffer> // En una API real, esto suele ser una respuesta envuelta (ResponseDTO)
}