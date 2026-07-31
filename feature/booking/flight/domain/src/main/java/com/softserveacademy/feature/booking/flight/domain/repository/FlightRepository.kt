package com.softserveacademy.feature.booking.flight.domain.repository

import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.domain.model.FlightOffer
import com.softserveacademy.core.domain.model.PassengerType
import kotlinx.coroutines.flow.Flow


/**
 * Interface definition for flight-related data operations.
 */
interface FlightRepository {
    /**
     * Searches for available flight offers based on criteria.
     *
     * @param origin IATA code of the departure airport.
     * @param destination IATA code of the arrival airport.
     * @param passengerCounts Map of passenger types and their respective counts.
     * @return A Flow emitting a list of flight offers.
     */
    fun searchFlights(
        origin: String,
        destination: String,
        passengerCounts: Map<PassengerType, Int>
    ): Flow<List<FlightOffer>>

    /**
     * Retrieves a list of airports matching the search query.
     *
     * @param query The text to filter airports by code or city.
     */
    fun searchAirports(query: String): Flow<List<Airport>>
}
