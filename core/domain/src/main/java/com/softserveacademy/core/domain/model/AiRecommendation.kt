package com.softserveacademy.core.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing a recommendation from the AI assistant.
 *
 * @property name The name of the recommended place.
 * @property latitude The latitude coordinate of the place.
 * @property longitude The longitude coordinate of the place.
 * @property description A description of why this place is recommended.
 * @property type The type of place (e.g., Park, Cafe, Viewpoint).
 * @property imageUrl An optional URL for a photo of the place.
 */
@Serializable
data class AiRecommendation(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val type: String,
    val imageUrl: String? = null
) : java.io.Serializable
