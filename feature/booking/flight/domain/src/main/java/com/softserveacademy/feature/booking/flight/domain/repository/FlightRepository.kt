package com.softserveacademy.feature.booking.flight.domain.repository

import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.domain.model.FlightOffer
import com.softserveacademy.core.domain.model.PassengerType
import kotlinx.coroutines.flow.Flow

/**
 * Interface definition for flight-related data operations.
 * Decouples the domain layer from specific data sources (Mock, Supabase, etc.).
 */
interface FlightRepository {
    /**
     * Searches for available flight offers based on specific criteria.
     *
     * @param origin IATA code of the departure airport.
     * @param destination IATA code of the arrival airport.
     * @param passengerCounts Map of passenger types (Adult, Child, Infant) and their respective counts.
     * @param cabinClass Preference for travel class (Economy, Business, etc.).
     * @param departureDate Epoch millis for the departure flight.
     * @param returnDate Epoch millis for the return flight (used primarily for package search).
     * @return A Flow emitting a list of matching flight offers.
     */
    fun searchFlights(
        origin: String,
        destination: String,
        passengerCounts: Map<PassengerType, Int>,
        cabinClass: com.softserveacademy.core.domain.model.CabinClass,
        departureDate: Long?,
        returnDate: Long?
    ): Flow<List<FlightOffer>>

    /**
     * Retrieves a list of airports matching the user's search query.
     * Used for the autocomplete/suggestions feature.
     *
     * @param query The text to filter airports by IATA code, city, or name.
     * @return A Flow emitting matching airports.
     */
    fun searchAirports(query: String): Flow<List<Airport>>
}
