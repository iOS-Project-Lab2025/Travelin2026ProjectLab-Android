package com.softserveacademy.feature.favorites.hotels.domain.model
import com.softserveacademy.core.domain.model.Hotel

data class FavoriteHotel(
    val id: String,
    val hotel: Hotel,
    val isAvailable: Boolean = true,
    val roomType: String = "1 Bed"
)