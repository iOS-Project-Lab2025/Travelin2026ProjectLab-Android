package com.softserveacademy.feature.booking.flight.domain.usecase


import com.softserveacademy.core.domain.model.FlightOffer

import com.softserveacademy.feature.booking.flight.domain.repository.FlightRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to search for available flights based on specific traveler criteria.
 * This class coordinates the communication between the Presentation layer and the Repository.
 */
class SearchFlightsUseCase @Inject constructor(private val repository: FlightRepository) {
    operator fun invoke(
        origin: String,
        destination: String,
        passengerCounts: com.softserveacademy.core.domain.model.PassengerCounts, // CAMBIO
        cabinClass: com.softserveacademy.core.domain.model.CabinClass,
        departureDate: Long?,
        returnDate: Long?
    ): Flow<List<FlightOffer>> {
        return repository.searchFlights(origin, destination, passengerCounts, cabinClass, departureDate, returnDate)
    }
}