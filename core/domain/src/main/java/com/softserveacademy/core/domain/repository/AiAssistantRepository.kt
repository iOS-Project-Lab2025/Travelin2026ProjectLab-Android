package com.softserveacademy.core.domain.repository

import com.softserveacademy.core.domain.model.AiRecommendation
import com.softserveacademy.core.error.model.AppResult

/**
 * Repository interface for interacting with the AI Assistant (Koog).
 */
interface AiAssistantRepository {
    /**
     * Fetches recommendations based on a voice command and current location.
     *
     * @param query The text recognized from voice input.
     * @param latitude The current latitude.
     * @param longitude The current longitude.
     * @return A list of [AiRecommendation] wrapped in [AppResult].
     */
    suspend fun getRecommendations(
        query: String,
        latitude: Double,
        longitude: Double
    ): AppResult<List<AiRecommendation>>
}
