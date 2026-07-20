package com.softserveacademy.core.domain.repository

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.HotelDetails
import com.softserveacademy.core.domain.model.HotelRoom

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
    suspend fun getHotelById(id: Int): HotelDetails

    /**
     * Fetch all available hotels.
     *
     * @return A list of all hotel objects.
     */
    suspend fun getHotels(): List<Hotel>

    /**
     * Fetch all rooms for a specific hotel.
     * @param hotelId The ID of the hotel.
     * @param checkInDate The check-in date in milliseconds.
     * @param checkOutDate The check-out date in milliseconds.
     * @return A list of rooms for the hotel.
     */
    suspend fun getHotelRooms(hotelId: Int, checkInDate: Long, checkOutDate: Long): List<HotelRoom>

    /**
     * Reserve a room in a hotel.
     * @param hotelId The ID of the hotel.
     * @param roomId The ID of the room.
     * @param checkInDate The check-in date in milliseconds.
     * @param checkOutDate The check-out date in milliseconds.
     */
    suspend fun reserveRoom(hotelId: Int, roomId: Int, checkInDate: Long, checkOutDate: Long)
}