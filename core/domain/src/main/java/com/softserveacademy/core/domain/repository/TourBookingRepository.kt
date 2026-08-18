package com.softserveacademy.core.domain.repository

import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.model.TourBooking
import com.softserveacademy.core.error.model.AppResult

/**
 * Interface for managing tour bookings.
 */
interface TourBookingRepository {
    /**
     * Save a new tour booking or update an existing one.
     */
    suspend fun saveBooking(booking: TourBooking): AppResult<Unit>

    /**
     * Retrieve all tour bookings.
     */
    suspend fun getBookings(): AppResult<List<TourBooking>>

    /**
     * Retrieve a tour booking by its ID.
     */
    suspend fun getBookingById(bookingId: String): AppResult<TourBooking?>

    /**
     * Update the status of a specific tour booking.
     */
    suspend fun updateBookingStatus(bookingId: String, status: BookingStatus): AppResult<Unit>
}
