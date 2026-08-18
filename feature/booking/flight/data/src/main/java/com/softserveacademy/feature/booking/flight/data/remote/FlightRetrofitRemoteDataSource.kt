package com.softserveacademy.feature.booking.flight.data.remote

import com.softserveacademy.core.domain.model.*
import javax.inject.Inject

/**
 * Remote implementation using Retrofit or any real network client.
 * Currently serves as a placeholder that forces a fallback to mocks.
 */
class RetrofitFlightRemoteDataSource @Inject constructor() : FlightRemoteDataSource {

    override suspend fun getFlightOffers(
        origin: String,
        destination: String,
        passengerCounts: PassengerCounts,
        cabinClass: CabinClass,
        departureDate: Long?,
        returnDate: Long?
    ): List<FlightOffer> {
        // Here you would call your Retrofit service
        // Throwing an exception forces the Repository to use the Mock backup
        throw IllegalStateException("Real API not implemented yet")
    }

    override suspend fun searchAirports(query: String): List<Airport> {
        throw IllegalStateException("Real API not implemented yet")
    }
}