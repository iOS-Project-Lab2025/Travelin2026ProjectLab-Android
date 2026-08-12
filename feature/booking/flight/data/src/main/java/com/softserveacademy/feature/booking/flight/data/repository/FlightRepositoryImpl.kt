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
 * Acts as a bridge between the [FlightRemoteDataSource] and the Domain layer.
 *
 * Future improvement: This repository can be easily extended to include
 * a local cache or a real API while keeping the Domain layer untouched.
 *
 * @param remoteDataSource The data source providing flight offers and airport data.
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