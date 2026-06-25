package com.softserveacademy.core.domain

data class Hotel(
    val id: Int? = null,
    val name: String,
    val address: String,
    val star: Int? = null,
    val userRating: Double? = null,
    val pricePerNight: Int,
    val image: Int
)