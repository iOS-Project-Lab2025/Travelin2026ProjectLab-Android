package com.softserveacademy.feature.booking.flight.data.repository

import com.softserveacademy.core.domain.model.FlightBooking
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingRepository
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * Data layer implementation for managing finalized flight bookings.
 *
 * Handles the persistence of official booking records. Currently simulates
 * network latency and successful storage.
 */
class FlightBookingRepositoryImpl @Inject constructor() : FlightBookingRepository {

    /**
     * Officializes a flight booking by saving it to the persistent storage.
     */
    override suspend fun saveBooking(booking: FlightBooking): AppResult<Unit> {
        // Here is where you would call Supabase or your API Client
        delay(1000.milliseconds) // Simulate network delay
        return AppResult.Success(Unit)
    }
}