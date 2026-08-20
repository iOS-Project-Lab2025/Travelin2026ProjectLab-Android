package com.softserveacademy.feature.favorites.hotels.presentation.states

import com.softserveacademy.feature.favorites.hotels.domain.model.FavoriteHotel

data class FavoriteHotelsState(
    val isLoading: Boolean = false,
    val hotels: List<FavoriteHotel> = emptyList(),
    val filteredHotels: List<FavoriteHotel> = emptyList(),
    val selectedFilter: String = "Available",
    val error: String? = null
)