package com.softserveacademy.core.domain.usecase

import com.softserveacademy.core.domain.model.AiRecommendation
import com.softserveacademy.core.domain.repository.AiAssistantRepository
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for getting personalized recommendations from the AI assistant.
 */
class GetAiRecommendationsUseCase @Inject constructor(
    private val aiAssistantRepository: AiAssistantRepository
) {
    suspend operator fun invoke(
        query: String,
        latitude: Double,
        longitude: Double
    ): AppResult<List<AiRecommendation>> {
        return aiAssistantRepository.getRecommendations(query, latitude, longitude)
    }
}
