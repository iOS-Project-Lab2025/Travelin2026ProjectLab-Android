package com.softserveacademy.core.domain.model

data class Destination(
    val id: String,
    val imageUrl: String,
    val name: String,
    val location: String,
    val rating: Double,
    val pricePerPax: Double,
    val currency: String,
    val durationLabel: String // "3D2N"
)
