package com.softserveacademy.core.domain.repository

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.HotelDetails
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
    suspend fun getHotelById(id: Int): AppResult<HotelDetails>

    /**
     * Fetch all available hotels.
     *
     * @return A list of all hotel objects.
     */
    suspend fun getHotels(): AppResult<List<Hotel>>

    /**
     * Fetch all rooms for a specific hotel.
     * @param hotelId The ID of the hotel.
     * @param checkInDate The check-in date in milliseconds.
     * @param checkOutDate The check-out date in milliseconds.
     * @param guestCount The number of guests for the booking.
     * @return A list of rooms for the hotel.
     */
    suspend fun getHotelRooms(hotelId: Int, checkInDate: Long, checkOutDate: Long, guestCount: Int): AppResult<List<HotelRoom>>

    /**
     * Reserve a room in a hotel.
     * @param hotelId The ID of the hotel.
     * @param roomId The ID of the room.
     * @param checkInDate The check-in date in milliseconds.
     * @param checkOutDate The check-out date in milliseconds.
     */
    suspend fun reserveRoom(hotelId: Int, roomId: Int, checkInDate: Long, checkOutDate: Long): AppResult<Unit>
}