package com.softserveacademy.home.presentation.model

data class TourUi(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val location: String,
    val rating: Double,
    val price: String,
    val duration: String
)
