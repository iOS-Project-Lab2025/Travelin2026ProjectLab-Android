package com.softserveacademy.feature.favorites.hotels.presentation.events

import com.softserveacademy.feature.favorites.hotels.domain.model.FavoriteHotel

sealed interface FavoriteHotelsEvent {
    data class OnFilterSelected(val filter: String) : FavoriteHotelsEvent
    data class OnHotelClick(val hotel: FavoriteHotel) : FavoriteHotelsEvent
    data object OnBackClick : FavoriteHotelsEvent
}