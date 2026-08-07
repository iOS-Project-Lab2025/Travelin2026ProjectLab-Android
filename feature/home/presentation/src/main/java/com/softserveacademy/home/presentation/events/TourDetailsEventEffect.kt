package com.softserveacademy.home.presentation.events

import com.softserveacademy.core.domain.model.Tour

/**
 * On click effects that happens one time for the Tour Detail screen.
 */
sealed interface TourDetailsEventEffect {
    data object NavigateBack : TourDetailsEventEffect
    data class ShareTour(val tour: Tour) : TourDetailsEventEffect
    data class NavigateToGallery(val imageList: List<String>) : TourDetailsEventEffect
}