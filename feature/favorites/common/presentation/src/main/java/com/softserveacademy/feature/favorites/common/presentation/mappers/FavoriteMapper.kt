package com.softserveacademy.feature.favorites.common.presentation.mappers

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.feature.favorites.common.domain.model.FavoriteItem

fun FavoriteItem.toHotel(): Hotel {
    return Hotel(
        name = this.title,
        address = this.location,
        userRating = this.rating,
        pricePerNight = this.price,
        image = listOf(this.imageUrl)
    )
}