package com.softserveacademy.feature.booking.flight.data.repository

import com.softserveacademy.core.domain.model.FlightOffer
import com.softserveacademy.core.domain.model.PassengerType
import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.domain.model.CabinClass
import com.softserveacademy.feature.booking.flight.data.remote.FlightRemoteDataSource
import com.softserveacademy.feature.booking.flight.domain.repository.FlightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Concrete implementation of [FlightRepository].
 * Orchestrates data between remote sources and domain consumers.
 */
class FlightRepositoryImpl @Inject constructor(
    private val remoteDataSource: FlightRemoteDataSource
) : FlightRepository {

    override fun searchFlights(
        origin: String,
        destination: String,
        passengerCounts: Map<PassengerType, Int>,
        cabinClass: CabinClass,
        departureDate: Long?,
        returnDate: Long?
    ): Flow<List<FlightOffer>> = flow {
        val results = remoteDataSource.getFlightOffers(
            origin, destination, passengerCounts, cabinClass, departureDate, returnDate
        )
        emit(results)
    }

    override fun searchAirports(query: String): Flow<List<Airport>> = flow {
        emit(remoteDataSource.searchAirports(query))
    }
}