package com.softserveacademy.feature.favorites.common.presentation.events

sealed interface TravelFavoritesEffect {
    data object NavigateToHotels : TravelFavoritesEffect
    data object NavigateToTours : TravelFavoritesEffect
    data class NavigateToDetail(val id: String, val type: FavoriteType) : TravelFavoritesEffect
    data object NavigateBack : TravelFavoritesEffect
}
