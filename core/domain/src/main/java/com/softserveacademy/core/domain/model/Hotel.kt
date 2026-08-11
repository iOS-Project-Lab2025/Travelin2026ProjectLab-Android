package com.softserveacademy.core.domain.model

import com.softserveacademy.core.domain.util.FlexibleListSerializer
import com.softserveacademy.core.domain.util.FlexibleStringSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Domain model representing the full details of a hotel.
 *
 * This class contains the complete information for a hotel, typically used on a detail screen.
 *
 * @property id The unique identifier of the hotel.
 * @property imageList A list of URLs for the hotel's image gallery.
 * @property name The name of the hotel.
 * @property address The physical address of the hotel.
 * @property starCategory The official star rating of the hotel.
 * @property numberOfReviews The total count of user reviews.
 * @property reviewRating The average user rating, usually on a scale of 0.0 to 5.0.
 * @property description A detailed text description of the hotel and its services.
 * @property amenities A list of amenities of a hotel.
 * @property rooms The list of rooms available in the hotel.
 */
@Serializable
data class Hotel(
    @Serializable(with = FlexibleStringSerializer::class)
    val id: String = "",
    val name: String = "",
    val address: String = "",
    @SerialName("star")
    val starCategory: Int = 0,
    @SerialName("rating")
    val reviewRating: Double = 0.0,
    val numberOfReviews: Int = 0,
    @SerialName("image")
    @Serializable(with = FlexibleListSerializer::class)
    val imageList: List<String> = emptyList(),
    val description: String = "",
    @SerialName("includedItems")
    val amenities: List<Amenities> = emptyList(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val rooms: List<HotelRoom> = emptyList(),
    val nearbyPlaces: List<Poi> = emptyList()
) : java.io.Serializable {
    val limitedReviews: String
        get() = if (numberOfReviews > 999) "999+ reviews" else "$numberOfReviews reviews"
    val limitedImages: String
        get() = if (imageList.size > 400) "400+ Photos" else "${imageList.size-1}+ Photos"

}

/**
 * Amenities included in the hotel.
 */
@Serializable
enum class Amenities{
    BuffetBreakfast,
    FreeWifi,
    FitnessCenter,
    Pool,
    CleaningServices,
    SelfParking,
    RoomService,
    AcUnit
}
