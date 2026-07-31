package com.softserveacademy.home.presentation.events

/**
 * Sealed interface representing user intents for the hotel detail screen.
 */
sealed interface HotelDetailsEvent {
    data class Load(val hotelId: Int) : HotelDetailsEvent
    data object NavigateBack : HotelDetailsEvent
    data object Share : HotelDetailsEvent
    data object ToggleFavorite : HotelDetailsEvent
    data object BookNow : HotelDetailsEvent
    data object ViewGallery : HotelDetailsEvent
    data object ViewFullMap : HotelDetailsEvent
    data object ToggleDescription : HotelDetailsEvent
    data object ViewAllAmenities : HotelDetailsEvent
    data object DismissAmenities : HotelDetailsEvent
    data object DismissMap : HotelDetailsEvent
}
