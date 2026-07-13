package com.softserveacademy.home.presentation.model

/**
 * UI model representing the detailed information of a hotel.
 *
 * @property id Unique identifier for the hotel.
 * @property imageList List of image URLs associated with the hotel.
 * @property name Name of the hotel.
 * @property numberOfReviews Total number of reviews received by the hotel.
 * @property rating Average rating of the hotel.
 * @property numberOfImages Total number of photos available for the hotel.
 * @property description Detailed description of the hotel.
 * @property minimumPrice Lower price per night for staying at the hotel.
 */
data class HotelDetailsUi (
    val id: Int,
    val minimumPrice: Int,
    val imageList: List<String> = emptyList(),
    val name: String,
    val numberOfReviews: Int,
    val numberOfImages: Int,
    val rating: Double,
    val description: String,
    val includedItems: List<IncludedItemUi> = emptyList()
){
    val limitedReviews: String
        get() = if (numberOfReviews > 100) "100+ reviews" else "$numberOfReviews reviews"
    val limitedImages: String
        get() = if (numberOfImages > 100) "100+ Photos" else "$numberOfImages Photos"
}


