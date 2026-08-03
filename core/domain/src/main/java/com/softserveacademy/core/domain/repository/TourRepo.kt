package com.softserveacademy.core.domain.repository

import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.error.model.AppResult

/**
 * Repository interface for managing tour data.
 */
interface TourRepo {
    /**
     * Fetches all available tours.
     * @return A list of [Tour] objects.
     */
    suspend fun getTours(): AppResult<List<Tour>>

    /**
     * Fetches a specific tour by its ID.
     * @param id The ID of the tour to fetch.
     * @return The [Tour] object if found.
     */
    suspend fun getTourById(id: String): AppResult<Tour>
}
