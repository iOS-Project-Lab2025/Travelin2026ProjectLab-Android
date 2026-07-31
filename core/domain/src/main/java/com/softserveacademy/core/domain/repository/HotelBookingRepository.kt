package com.softserveacademy.core.domain.repository

import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.model.HotelBooking
import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing hotel bookings.
 */
interface HotelBookingRepository {
    /**
     * Save a new hotel booking or update an existing one.
     */
    suspend fun saveBooking(booking: HotelBooking)

    /**
     * Retrieve all hotel bookings.
     */
    fun getBookings(): Flow<List<HotelBooking>>

    /**
     * Retrieve a hotel booking by its ID.
     */
    fun getBookingById(bookingId: String): Flow<HotelBooking?>

    /**
     * Update the status of a specific hotel booking.
     */
    suspend fun updateBookingStatus(bookingId: String, status: BookingStatus)
}
