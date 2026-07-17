package com.softserveacademy.core.domain.model

import kotlin.time.Duration

/**
 * Represents a tour or activity available at a destination.
 *
 * A tour contains the information required to present an activity to users,
 * including its description, duration, pricing, rating, and category.
 *
 * @property id Unique identifier of the tour.
 * @property title Display name of the tour.
 * @property description Brief description of the experience.
 * @property location Location where the tour takes place.
 * @property imageUrl URL of the tour's representative image.
 * @property duration Estimated duration of the tour.
 * @property price Price per participant.
 * @property rating Average user rating of the tour.
 * @property category Category of the tour.
 */
data class Tour(
    val id: String,

    val title: String,

    val description: String,

    val location: String,

    val imageUrl: String,

    val duration: Duration,

    val price: Double,

    val rating: Float,

    val category: TourCategory
)

/**
 * Represents the available categories of a tour.
 *
 * @property ADVENTURE Outdoor and adventure experiences.
 * @property CULTURE Historical, artistic, or cultural experiences.
 * @property GASTRONOMY Food and beverage experiences.
 * @property NATURE Nature and wildlife experiences.
 * @property CITY City sightseeing and urban exploration.
 * @property FAMILY Activities suitable for families and children.
 */
enum class TourCategory {
    ADVENTURE,
    CULTURE,
    GASTRONOMY,
    NATURE,
    CITY,
    FAMILY
}