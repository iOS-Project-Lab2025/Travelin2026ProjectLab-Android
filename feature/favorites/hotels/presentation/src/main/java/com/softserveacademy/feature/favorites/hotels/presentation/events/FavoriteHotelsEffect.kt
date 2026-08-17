package com.softserveacademy.feature.favorites.hotels.presentation.events

sealed interface FavoriteHotelsEffect {
    data class NavigateToHotelDetail(val hotelId: String) : FavoriteHotelsEffect
    data object NavigateBack : FavoriteHotelsEffect
}
