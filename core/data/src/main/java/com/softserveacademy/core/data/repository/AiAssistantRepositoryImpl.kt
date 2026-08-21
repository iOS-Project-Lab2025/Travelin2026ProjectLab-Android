package com.softserveacademy.core.data.repository

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import ai.koog.agents.core.agent.AIAgent
import ai.koog.http.client.ktor.KtorKoogHttpClient
import ai.koog.prompt.executor.clients.google.GoogleLLMClient
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.MultiLLMPromptExecutor
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.LLModel
import com.softserveacademy.core.data.BuildConfig
import com.softserveacademy.core.domain.model.AiRecommendation
import com.softserveacademy.core.domain.repository.AiAssistantRepository
import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.AppResult
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import kotlinx.serialization.json.Json
import java.security.MessageDigest
import javax.inject.Inject

/**
 * Implementation of [AiAssistantRepository] that connects to the AI assistant API using the Koog AI framework.
 */
class AiAssistantRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json
) : AiAssistantRepository {
    
    private val ktorClient by lazy {
        val sha1 = getSha1Signature(context)
        Log.d("AiAssistantRepo", "App Package: ${context.packageName}")
        Log.d("AiAssistantRepo", "Sending SHA-1 to Google: $sha1")
        
        HttpClient(OkHttp) {
            defaultRequest {
                header("X-Android-Package", context.packageName)
                sha1?.let { 
                    header("X-Android-Cert", it)
                }
            }
        }
    }

    override suspend fun getRecommendations(
        query: String,
        latitude: Double,
        longitude: Double
    ): AppResult<List<AiRecommendation>> {
        return try {
            val apiKey = BuildConfig.AI_KEY
            
            // Defining Gemini 3.6 Flash manually since it's newer than the library version
            val gemini36Flash = LLModel(
                provider = LLMProvider.Google,
                id = "gemini-3.6-flash",
                capabilities = GoogleModels.Gemini2_5Flash.capabilities,
                contextLength = 1_048_576,
                maxOutputTokens = 65_536
            )

            val agent = AIAgent(
                promptExecutor = MultiLLMPromptExecutor(
                    GoogleLLMClient(
                        apiKey = apiKey,
                        httpClientFactory = KtorKoogHttpClient.Factory(baseClient = ktorClient)
                    )
                ),
                llmModel = gemini36Flash
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

    private fun getSha1Signature(context: Context): String? {
        return try {
            val packageInfo = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            }

            val signatures = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                packageInfo.signingInfo?.apkContentsSigners
            } else {
                @Suppress("DEPRECATION")
                packageInfo.signatures
            }

            val signature = signatures?.firstOrNull() ?: return null
            val md = MessageDigest.getInstance("SHA-1")
            val digest = md.digest(signature.toByteArray())
            digest.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            Log.e("AiAssistantRepo", "Error getting SHA-1 signature", e)
            null
        }
    }
}
