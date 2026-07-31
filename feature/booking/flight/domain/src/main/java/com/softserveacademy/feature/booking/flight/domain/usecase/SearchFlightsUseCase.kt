package com.softserveacademy.feature.booking.flight.domain.usecase

import com.softserveacademy.core.domain.model.FlightOffer
import com.softserveacademy.core.domain.model.PassengerType
import com.softserveacademy.feature.booking.flight.domain.repository.FlightRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Business logic to search for flights.
 *
 * This use case orchestrates the search process by validating input
 * and calling the repository to fetch data.
 */
class SearchFlightsUseCase @Inject constructor(
    private val repository: FlightRepository
) {
    /**
     * Executes the search operation.
     */
    operator fun invoke(
        origin: String,
        destination: String,
        passengerCounts: Map<PassengerType, Int>
    ): Flow<List<FlightOffer>> {
        // Here we could add validation logic (e.g., origin != destination)
        return repository.searchFlights(origin, destination, passengerCounts)
    }
}