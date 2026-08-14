package com.softserveacademy.core.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing a place of interest near a hotel or destination.
 *
 * @property name The name of the place.
 * @property type The type of place (e.g., Museum, Park, Restaurant).
 * @property travelTime The estimated time to trave to the place from the hotel (e.g., "5 min").
 * @property imageUrl An optional URL for a photo of the place.
 * @property description A short summary or description of the place.
 */
@Serializable
data class Poi(
    val name: String,
    val type: String,
    val travelTime: String,
    val imageUrl: String? = null,
    val description: String? = null
) : java.io.Serializable
