package com.softserveacademy.core.domain.model

/**
 * Data class representing a hotel.
 *
 * @param id The unique identifier of the hotel.
 * @param name The name of the hotel.
 * @param address The address of the hotel.
 * @param star The number of stars the hotel has.
 * @param userRating The user rating of the hotel.
 * @param pricePerNight The price per night for the hotel.
 * @param image The image source of the hotel.
 * @param imagesList A list of image URLs associated with the hotel.As default is an empty list so you don´t
 * have to give it a list of images to compile.
 * @param rooms The list of rooms available in the hotel.
 */
data class Hotel(
    val id: Int? = null,
    val name: String,
    val address: String,
    val star: Int? = null,
    val userRating: Double? = null,
    val pricePerNight: Int,
    val image: Int,
    val imagesList: List<String> = emptyList(),
    val rooms: List<HotelRoom> = emptyList()
)