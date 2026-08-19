package com.softserveacademy.feature.favorites.tours.presentation.states

import com.softserveacademy.feature.favorites.tours.domain.model.FavoriteTour

data class FavoritesToursState(
    val isLoading: Boolean = false,
    val tours: List<FavoriteTour> = emptyList(),
    val filteredTours: List<FavoriteTour> = emptyList(),
    val selectedFilter: String = "All",
    val error: String? = null
)
