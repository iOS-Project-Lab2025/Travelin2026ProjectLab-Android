package com.softserveacademy.feature.booking.flight.domain.usecase

import com.softserveacademy.core.domain.model.FlightOffer
import com.softserveacademy.core.domain.model.PassengerType
import com.softserveacademy.feature.booking.flight.domain.repository.FlightRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to search for available flights based on specific traveler criteria.
 * This class coordinates the communication between the Presentation layer and the Repository.
 */
class SearchFlightsUseCase @Inject constructor(
    private val repository: FlightRepository
) {
    /**
     * Executes the search operation by delegating to the repository.
     *
     * @param origin Departure airport code.
     * @param destination Arrival airport code.
     * @param passengerCounts Breakdown of travelers by type.
     * @param cabinClass Selection of travel comfort (Economy, etc.).
     * @param departureDate Epoch millis of the outbound flight.
     * @param returnDate Epoch millis of the inbound flight (optional).
     * @return A Flow emitting matching [FlightOffer] results.
     */
    operator fun invoke(
        origin: String,
        destination: String,
        passengerCounts: Map<PassengerType, Int>,
        cabinClass: com.softserveacademy.core.domain.model.CabinClass,
        departureDate: Long?,
        returnDate: Long?
    ): Flow<List<FlightOffer>> {
        return repository.searchFlights(origin, destination, passengerCounts, cabinClass, departureDate, returnDate)
    }
}