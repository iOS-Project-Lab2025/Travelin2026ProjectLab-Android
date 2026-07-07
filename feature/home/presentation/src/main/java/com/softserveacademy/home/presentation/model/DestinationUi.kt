package com.softserveacademy.home.presentation.model

data class DestinationUi(
    val id: String,
    val imageUrl: String,
    val name: String,
    val location: String,
    val rating: Double,
    val priceFrom: String,
    val duration: String // "3D2N"
)