package com.softserveacademy.home.presentation.events

import com.softserveacademy.core.domain.model.AiRecommendation

/**
 * Sealed interface representing user intents for the hotel detail screen.
 */
sealed interface HotelDetailsEvent {
    data class Load(val id: String) : HotelDetailsEvent
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
    data object ViewExploreArea : HotelDetailsEvent
    data object DismissExploreArea : HotelDetailsEvent
    data object RetryPois : HotelDetailsEvent
    data class VoiceSearch(val query: String) : HotelDetailsEvent
    data class SelectRecommendation(val recommendation: AiRecommendation?) : HotelDetailsEvent
    data object ClearVoiceQuery : HotelDetailsEvent
    data object ClearAiRecommendations : HotelDetailsEvent
}
