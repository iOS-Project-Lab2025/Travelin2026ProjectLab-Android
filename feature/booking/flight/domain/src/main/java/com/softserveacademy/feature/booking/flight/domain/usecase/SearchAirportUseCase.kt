package com.softserveacademy.feature.booking.flight.domain.usecase

import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.feature.booking.flight.domain.repository.FlightRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to search for airports based on a text query.
 * Orchestrates the search by delegating to the FlightRepository.
 */
class SearchAirportsUseCase @Inject constructor(
    private val repository: FlightRepository
) {
    /**
     * Executes the airport search logic.
     */
    operator fun invoke(query: String): Flow<List<Airport>> = repository.searchAirports(query)
}