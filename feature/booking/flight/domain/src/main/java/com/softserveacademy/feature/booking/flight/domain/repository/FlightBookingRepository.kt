package com.softserveacademy.feature.booking.flight.domain.repository

import com.softserveacademy.core.domain.model.FlightBooking
import com.softserveacademy.core.error.model.AppResult

/**
 * Interface for managing finalized flight bookings.
 */
interface FlightBookingRepository {
    /**
     * Officializes a flight booking by saving it to persistent storage.
     */
    suspend fun saveBooking(booking: FlightBooking): AppResult<FlightBooking>
}