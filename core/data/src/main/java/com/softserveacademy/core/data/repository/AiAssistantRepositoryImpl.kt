package com.softserveacademy.core.data.repository

import com.softserveacademy.core.domain.model.AiRecommendation
import com.softserveacademy.core.domain.repository.AiAssistantRepository
import com.softserveacademy.core.error.model.AppResult
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Implementation of [AiAssistantRepository] that connects to the AI assistant API.
 * Currently uses mock data for demonstration.
 */
class AiAssistantRepositoryImpl @Inject constructor() : AiAssistantRepository {
    override suspend fun getRecommendations(
        query: String,
        latitude: Double,
        longitude: Double
    ): AppResult<List<AiRecommendation>> {
        // Simulating network delay
        delay(2000)
        
        // Mock data based on query keywords
        val lowerQuery = query.lowercase()
        val recommendations = when {
            lowerQuery.contains("tranquilo") || lowerQuery.contains("quiet") -> {
                listOf(
                    AiRecommendation(
                        name = "Parque de la Paz",
                        latitude = latitude + 0.005,
                        longitude = longitude + 0.005,
                        description = "Un lugar muy tranquilo ideal para leer o meditar.",
                        type = "Parque"
                    ),
                    AiRecommendation(
                        name = "Mirador del Silencio",
                        latitude = latitude - 0.005,
                        longitude = longitude - 0.005,
                        description = "Vistas espectaculares con muy poco ruido ambiental.",
                        type = "Mirador"
                    )
                )
            }
            lowerQuery.contains("comida") || lowerQuery.contains("food") -> {
                listOf(
                    AiRecommendation(
                        name = "Café Escondido",
                        latitude = latitude + 0.002,
                        longitude = longitude - 0.003,
                        description = "Pequeño café con repostería artesanal.",
                        type = "Cafetería"
                    )
                )
            }
            else -> emptyList()
        }
        
        return AppResult.Success(recommendations)
    }
}
