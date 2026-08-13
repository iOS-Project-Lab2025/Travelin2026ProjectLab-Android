package com.softserveacademy.core.domain.repository

import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.model.HotelBooking
import com.softserveacademy.core.error.model.AppResult

/**
 * Interface for managing hotel bookings.
 */
interface HotelBookingRepository {
    /**
     * Save a new hotel booking or update an existing one.
     */
    suspend fun saveBooking(booking: HotelBooking): AppResult<Unit>

    /**
     * Retrieve all hotel bookings.
     */
    suspend fun getBookings(): AppResult<List<HotelBooking>>

    /**
     * Retrieve a hotel booking by its ID.
     */
    suspend fun getBookingById(bookingId: String): AppResult<HotelBooking?>

    /**
     * Update the status of a specific hotel booking.
     */
    suspend fun updateBookingStatus(bookingId: String, status: BookingStatus): AppResult<Unit>
}
