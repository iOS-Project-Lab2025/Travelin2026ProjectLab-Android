package com.softserveacademy.core.domain.repository

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.HotelRoom

/**
 * Interface for fetching hotel data.
 */
interface HotelRepo{
    /**
     * Fetch a hotel by its ID.
     *
     * @param id The ID of the hotel to fetch.
     * @return The fetched hotel object.
     */
    suspend fun getHotelById(id: Int?): Hotel

    /**
     * Fetch all available hotels.
     *
     * @return A list of all hotel objects.
     */
    suspend fun getHotels(): List<Hotel>

    /**
     * Fetch all rooms for a specific hotel.
     * @param hotelId The ID of the hotel.
     * @return A list of rooms for the hotel.
     */
    suspend fun getHotelRooms(hotelId: Int): List<HotelRoom>

    /**
     * Reserve a room in a hotel.
     * @param hotelId The ID of the hotel.
     * @param roomId The ID of the room.
     */
    suspend fun reserveRoom(hotelId: Int, roomId: Int)
}