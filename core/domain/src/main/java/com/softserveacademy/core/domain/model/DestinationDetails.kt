package com.softserveacademy.core.domain.model

import java.io.Serializable

/**
 * Domain model representing the full details of a destination.
 *
 * This class contains the complete information used on a detail screen.
 *
 * @property id The unique identifier of the destination.
 * @property minimumPrice The starting price per night for the destination.
 * @property imageList A list of URLs for the destination's image gallery.
 * @property name The name of the destination.
 * @property address The physical address of the destination.
 * @property star The official star rating of the destination.
 * @property image The main thumbnail image URLs for the destination.
 * @property numberOfReviews The total count of user reviews.
 * @property rating The average user rating, usually on a scale of 0.0 to 5.0.
 * @property description A detailed text description of the destination and its services.
 * @property includedItems A list of amenities available at the destination.
 * @property latitude The geographical latitude of the destination.
 * @property longitude The geographical longitude of the destination.
 */
data class DestinationDetails(
    val id: Int,
    val minimumPrice: Int,
    val imageList: List<String>,
    val name: String,
    val address: String = "",
    val star: Int = 0,
    val image: List<String> = emptyList(),
    val numberOfReviews: Int,
    val rating: Double,
    val description: String,
    val includedItems: List<IncludedItems> = emptyList(),
    val latitude: Double,
    val longitude: Double
) : Serializable {
    val limitedReviews: String
        get() = if (numberOfReviews > 999) "999+ reviews" else "$numberOfReviews reviews"
    val limitedImages: String
        get() = if (imageList.size > 400) "400+ Photos" else "${imageList.size-1}+ Photos"
}
