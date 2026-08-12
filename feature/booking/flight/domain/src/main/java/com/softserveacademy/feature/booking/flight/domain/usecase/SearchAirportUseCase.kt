package com.softserveacademy.feature.booking.flight.domain.usecase

import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.feature.booking.flight.domain.repository.FlightRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to retrieve matching airports for a given query.
 * Primarily used for autocomplete features in origin/destination fields.
 */
class SearchAirportsUseCase @Inject constructor(
    private val repository: FlightRepository
) {
    /**
     * Searches for airports by city, country, or IATA code.
     *
     * @param query The user's input string.
     * @return A Flow emitting a list of matching [Airport] domain models.
     */
    operator fun invoke(query: String): Flow<List<Airport>> {
        return repository.searchAirports(query)
    }
}