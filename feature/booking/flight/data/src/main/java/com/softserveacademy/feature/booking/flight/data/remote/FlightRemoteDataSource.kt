package com.softserveacademy.feature.booking.flight.data.remote

import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.domain.model.FlightOffer
import com.softserveacademy.core.domain.model.PassengerCounts
import com.softserveacademy.core.domain.model.PassengerType

/**
 * Interface defining the remote operations for flight data.
 * Serves as the contract for any remote provider (Mock, Retrofit, Supabase).
 */
interface FlightRemoteDataSource {
    /**
     * Fetches available flight offers from a remote service.
     */
    suspend fun getFlightOffers(
        origin: String,
        destination: String,
        passengerCounts: PassengerCounts,
        cabinClass: com.softserveacademy.core.domain.model.CabinClass,
        departureDate: Long?,
        returnDate: Long?
    ): List<FlightOffer>

    /**
     * Retrieves airports matching a query string from the remote provider.
     */
    suspend fun searchAirports(query: String): List<Airport>
}