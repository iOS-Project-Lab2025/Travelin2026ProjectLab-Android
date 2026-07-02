package com.softserveacademy.core.domain.repository

import com.softserveacademy.core.domain.model.Hotel

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
}