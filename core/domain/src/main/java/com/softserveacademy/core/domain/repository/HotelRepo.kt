package com.softserveacademy.core.domain.repository

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.HotelBooking
import com.softserveacademy.core.domain.model.HotelRoom
import com.softserveacademy.core.error.model.AppResult

/**
 * Interface for fetching hotel data.
 */
interface HotelRepo{
    /**
     * Fetch the full details of a hotel by its ID.
     *
     * @param id The ID of the hotel to fetch.
     * @return The detailed hotel object.
     */
    suspend fun getHotelById(id: String): AppResult<Hotel>

    /**
     * Fetch all available hotels.
     *
     * @return A list of all hotel objects.
     */
    suspend fun getHotels(): AppResult<List<Hotel>>

    /**
     * Fetch all rooms for a specific hotel.
     * @param hotelId The ID of the hotel.
     * @return A list of rooms for the hotel.
     */
    suspend fun getHotelRooms(hotelId: String): AppResult<List<HotelRoom>>

    /**
     * Get all bookings register.
     * @return A list of bookings.
     */
    suspend fun getBookings(): List<HotelBooking>
}