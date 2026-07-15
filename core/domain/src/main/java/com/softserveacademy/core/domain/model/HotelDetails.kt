package com.softserveacademy.core.domain.model

/**
 * Domain model representing the full details of a hotel.
 *
 * This class contains the complete information for a hotel, typically used on a detail screen.
 *
 * @property id The unique identifier of the hotel.
 * @property minimumPrice The starting price per night for the hotel.
 * @property imageList A list of URLs for the hotel's image gallery.
 * @property name The name of the hotel.
 * @property numberOfReviews The total count of user reviews.
 * @property rating The average user rating, usually on a scale of 0.0 to 5.0.
 * @property description A detailed text description of the hotel and its services.
 * @property includedItems A list of booleans representing the availability of standard amenities.
 */
data class HotelDetails(
    val id: Int,
    val minimumPrice: Int,
    val imageList: List<String>,
    val name: String,
    val numberOfReviews: Int,
    val rating: Double,
    val description: String,
    val includedItems: List<IncludedItem> = emptyList()
)

enum class IncludedItem{
    BuffetBreakfast,
    FreeWifi,
    FitnessCenter,
    Pool,
    CleaningServices,
    SelfParking,
    RoomService,
    AcUnit
}
