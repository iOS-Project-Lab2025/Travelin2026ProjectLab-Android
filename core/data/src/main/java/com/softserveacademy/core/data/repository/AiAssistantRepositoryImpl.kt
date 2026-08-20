package com.softserveacademy.core.data.repository

import android.util.Log
import ai.koog.agents.core.agent.AIAgent
import ai.koog.prompt.executor.clients.google.GoogleLLMClient
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.MultiLLMPromptExecutor
import com.softserveacademy.core.data.BuildConfig
import com.softserveacademy.core.domain.model.AiRecommendation
import com.softserveacademy.core.domain.repository.AiAssistantRepository
import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.AppResult
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Implementation of [AiAssistantRepository] that connects to the AI assistant API using the Koog AI framework.
 */
class AiAssistantRepositoryImpl @Inject constructor(
    private val json: Json
) : AiAssistantRepository {
    override suspend fun getRecommendations(
        query: String,
        latitude: Double,
        longitude: Double
    ): AppResult<List<AiRecommendation>> {
        return try {
            val apiKey = BuildConfig.AI_KEY
            val agent = AIAgent(
                promptExecutor = MultiLLMPromptExecutor(GoogleLLMClient(apiKey)),
                llmModel = GoogleModels.Gemini2_0FlashLite001
            )

            val prompt = """
                You are a travel assistant. Suggest 3-5 places near latitude $latitude and longitude $longitude based on the user's query: "$query".
                Provide relevant and interesting recommendations.
                Return the response ONLY as a JSON array of objects with the following structure:
                [
                  {
                    "name": "Place Name",
                    "latitude": 1.23,
                    "longitude": 4.56,
                    "description": "Short description of why this place is recommended.",
                    "type": "Type of place (e.g. Park, Cafe, Restaurant, Museum)",
                    "imageUrl": null
                  }
                ]
                Do not include any other text, markdown formatting, or "```json" blocks in your response.
            """.trimIndent()

            val result = agent.run(prompt, null)
            Log.d("AiAssistantRepo", "Raw AI response: $result")
            
            // Basic cleaning in case the LLM still includes markdown code blocks
            val cleanResult = result.trim()
                .removePrefix("```json")
                .removeSuffix("```")
                .trim()
            
            Log.d("AiAssistantRepo", "Clean AI response: $cleanResult")

            val recommendations = json.decodeFromString<List<AiRecommendation>>(cleanResult)
            
            AppResult.Success(recommendations)
        } catch (e: Exception) {
            Log.e("AiAssistantRepo", "Error getting recommendations: ${e.message}", e)
            AppResult.Failure(AppError.Unknown(e))
        }
    }
}
