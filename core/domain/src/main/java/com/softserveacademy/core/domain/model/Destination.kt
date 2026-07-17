package com.softserveacademy.core.domain.model

/**
 * Represents a travel destination available to users.
 *
 * A destination contains the basic information displayed in listings and
 * recommendations, including its image, location, rating, pricing, and
 * typical trip duration.
 *
 * @property id Unique identifier of the destination.
 * @property imageUrl URL of the destination's representative image.
 * @property name Name of the destination.
 * @property location Geographic location of the destination (e.g. city, region, or country).
 * @property rating Average user rating of the destination.
 * @property pricePerPax Starting price per traveler.
 * @property currency ISO 4217 currency code for the destination's pricing (e.g. "USD", "EUR", "CLP").
 * @property durationLabel Recommended or typical trip duration displayed to users (e.g. "3D2N").
 */
data class Destination(
    val id: String,
    val imageUrl: String,
    val name: String,
    val location: String,
    val rating: Double,
    val pricePerPax: Double,
    val currency: String,
    val durationLabel: String // "3D2N"
)
