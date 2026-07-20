package com.softserveacademy.core.domain.model

/**
 * Data class representing a hotel room.
 *
 * @param id The unique identifier of the room.
 * @param type The room type (e.g., Standard Queen, Deluxe King, Suite).
 * @param description A brief description of the room location or view.
 * @param maxOccupancy The maximum number of persons allowed in the room.
 * @param bedType The type and number of beds in the room.
 * @param bedCount The number of beds in the room.
 * @param amenities A list of included amenities (e.g., Breakfast, Free Wi-Fi).
 * @param pricePerNight The price for the selected stay per night.
 * @param images A list of image URLs associated with the room for the gallery.
 * @param totalRooms The total number of rooms available.
 * @param availableRooms The number of rooms currently available.
 * @param isAvailable Whether the room is currently available.
 */
data class HotelRoom(
    val id: Int? = null,
    val type: String,
    val description: String,
    val maxOccupancy: String,
    val bedType: String,
    val bedCount: Int = 1,
    val amenities: List<HotelRoomAmenity>,
    val pricePerNight: Int,
    val images: List<String> = emptyList(),
    val totalRooms: Int = 5,
    val availableRooms: Int = 5,
    val isAvailable: Boolean = true
)
