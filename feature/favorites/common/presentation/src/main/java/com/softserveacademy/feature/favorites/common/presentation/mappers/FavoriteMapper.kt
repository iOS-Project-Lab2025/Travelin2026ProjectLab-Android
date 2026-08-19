package com.softserveacademy.feature.favorites.common.presentation.mappers

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.feature.favorites.common.domain.model.FavoriteItem

fun FavoriteItem.toHotel(): Hotel {
    return Hotel(
        id = this.id,
        name = this.title,
        address = this.location,
        pricePerNight = this.price ?: 0.0,
        reviewRating = this.rating,
        imageList = listOf(this.imageUrl),
    )
}

fun FavoriteItem.toTour(): Tour {
    return Tour(
        id = this.id,
        title = this.title,
        location = this.location,
        rating = this.rating,
        imageList = listOf(this.imageUrl),
    )
}
