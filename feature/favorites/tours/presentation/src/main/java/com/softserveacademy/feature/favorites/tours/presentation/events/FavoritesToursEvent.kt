package com.softserveacademy.feature.favorites.tours.presentation.events

import com.softserveacademy.feature.favorites.tours.domain.model.FavoriteTour

sealed interface FavoritesToursEvent {
    data class OnFilterSelected(val filter: String) : FavoritesToursEvent
    data class OnTourClick(val tour: FavoriteTour) : FavoritesToursEvent
    data object OnBackClick : FavoritesToursEvent
}

sealed interface FavoritesToursEffect {
    data class NavigateToTourDetail(val tourId: String) : FavoritesToursEffect
    data object NavigateBack : FavoritesToursEffect
}
