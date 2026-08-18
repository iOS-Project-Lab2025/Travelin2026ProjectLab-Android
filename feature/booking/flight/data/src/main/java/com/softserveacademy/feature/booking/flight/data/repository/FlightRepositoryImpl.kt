package com.softserveacademy.feature.booking.flight.data.repository

import android.util.Log
import com.softserveacademy.core.domain.model.*
import com.softserveacademy.feature.booking.flight.data.di.MockApi
import com.softserveacademy.feature.booking.flight.data.di.RemoteApi
import com.softserveacademy.feature.booking.flight.data.remote.FlightRemoteDataSource
import com.softserveacademy.feature.booking.flight.domain.repository.FlightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Resilient implementation of [FlightRepository].
 * Coordinates between a real API and a Mock fallback to ensure high availability.
 */
class FlightRepositoryImpl @Inject constructor(
    @RemoteApi private val apiDataSource: FlightRemoteDataSource,
    @MockApi private val mockDataSource: FlightRemoteDataSource
) : FlightRepository {

    override fun searchFlights(
        origin: String,
        destination: String,
        passengerCounts: PassengerCounts,
        cabinClass: CabinClass,
        departureDate: Long?,
        returnDate: Long?
    ): Flow<List<FlightOffer>> = flow {
        try {
            // Step 1: Try the real API
            val results = apiDataSource.getFlightOffers(
                origin, destination, passengerCounts, cabinClass, departureDate, returnDate
            )
            emit(results)
        } catch (e: Exception) {
            // Step 2: Fallback to Mock if API fails or is not implemented
            Log.w("FlightRepo", "Real API failed: ${e.message}. Falling back to mocks.")
            emit(mockDataSource.getFlightOffers(
                origin, destination, passengerCounts, cabinClass, departureDate, returnDate
            ))
        }
    }

    override fun searchAirports(query: String): Flow<List<Airport>> = flow {
        try {
            emit(apiDataSource.searchAirports(query))
        } catch (e: Exception) {
            emit(mockDataSource.searchAirports(query))
        }
    }
}