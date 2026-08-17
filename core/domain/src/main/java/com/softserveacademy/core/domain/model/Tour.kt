package com.softserveacademy.core.domain.model

import com.softserveacademy.core.domain.util.DurationSerializer
import com.softserveacademy.core.domain.util.FlexibleListSerializer
import com.softserveacademy.core.domain.util.FlexibleStringSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
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
 * @property imageList List of URLs for the tour's images.
 * @property duration Estimated duration of the tour.
 * @property rates Rate per participant (adults, children, babies).
 * @property rating Average user rating of the tour.
 * @property category Category of the tour.
 * @property numberOfReviews Number of reviews for the tour.
 * @property includedServices List of included services.
 * @property latitude Latitude coordinate of the tour's location.
 * @property longitude Longitude coordinate of the tour's location.
 */
@Serializable
data class Tour(
    @Serializable(with = FlexibleStringSerializer::class)
    val id: String,
    val title: String = "",
    val description: String = "",
    val location: String = "",
    @SerialName("image_url")
    @Serializable(with = FlexibleListSerializer::class)
    val imageList: List<String> = emptyList(),
    @Serializable(with = DurationSerializer::class)
    val duration: Duration = Duration.ZERO,
    val rates: RatePerParticipant = RatePerParticipant(),
    val rating: Double = 0.0,
    val category: TourCategory = TourCategory.ADVENTURE,
    @SerialName("number_of_reviews")
    val numberOfReviews: Int = 0,
    @SerialName("included_services")
    @Serializable(with = FlexibleListSerializer::class)
    val includedServices: List<String> = emptyList(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,

){
    val limitedReviews: String
        get() = if (numberOfReviews > 999) "999+ reviews" else "$numberOfReviews reviews"
}

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
@Serializable
enum class TourCategory {
    ADVENTURE,
    CULTURE,
    GASTRONOMY,
    NATURE,
    CITY,
    FAMILY
}

/**
 * Represents the rate per participant for a tour.
 *
 * @property adults The rate for adults.
 * @property children The rate for children.
 * @property babies The rate for babies.
 */
@Serializable
data class RatePerParticipant(
    val adults: Double = 0.0,
    val children: Double = 0.0,
    val babies: Double = 0.0
)
