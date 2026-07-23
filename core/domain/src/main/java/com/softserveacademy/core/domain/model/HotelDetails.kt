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
 * @property address The physical address of the hotel.
 * @property star The official star rating of the hotel.
 * @property image The main thumbnail image URLs for the hotel.
 * @property numberOfReviews The total count of user reviews.
 * @property rating The average user rating, usually on a scale of 0.0 to 5.0.
 * @property description A detailed text description of the hotel and its services.
 * @property includedItems A list of booleans representing the availability of standard amenities.
 * @property rooms The list of rooms available in the hotel.
 */
data class HotelDetails(
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
    val includedItems: List<IncludedItem> = emptyList(),
    val latitude: Double,
    val longitude: Double,
    val rooms: List<HotelRoom> = emptyList()
) {
    /**
     * Converts the detailed hotel model into a simplified summary model.
     *
     * @return A [Hotel] object containing the essential information for lists or carousels.
     */
    fun toSummary(): Hotel {
        return Hotel(
            id = id,
            name = name,
            address = address,
            star = star,
            userRating = rating,
            pricePerNight = minimumPrice,
            image = image,
            imagesList = imageList,
            rooms = rooms
        )
    }
}

/**
 * Amenities included in the hotel.
 */
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
