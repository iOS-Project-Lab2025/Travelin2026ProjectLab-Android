package com.softserveacademy.home.presentation.events

/**
 * Sealed interface representing user intents for the tour detail screen.
 */
sealed interface TourDetailsEvent {
    data class Load(val id: String) : TourDetailsEvent
    data object NavigateBack : TourDetailsEvent
    data object Share : TourDetailsEvent
    data object ToggleFavorite : TourDetailsEvent
    data object ViewGallery : TourDetailsEvent
    data object ViewFullMap : TourDetailsEvent
    data object ToggleDescription : TourDetailsEvent
    data object DismissMap : TourDetailsEvent
}