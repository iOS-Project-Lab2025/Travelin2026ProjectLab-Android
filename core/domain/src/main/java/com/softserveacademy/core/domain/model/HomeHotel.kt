package com.softserveacademy.core.domain.model

data class HomeHotel(
    val id: String,
    val imageUrl: String,
    val name: String,
    val address: String,
    val starRating: Int,
    val pricePerNight: Double,
    val currency: String
)