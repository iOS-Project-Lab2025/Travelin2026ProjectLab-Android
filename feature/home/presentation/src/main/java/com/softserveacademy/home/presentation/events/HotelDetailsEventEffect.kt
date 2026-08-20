package com.softserveacademy.home.presentation.events

import com.softserveacademy.core.domain.model.Hotel

/**
 * On click effects that happens one time for the Hotel Detail screen.
 */
sealed interface HotelDetailsEventEffect {
    data object NavigateBack : HotelDetailsEventEffect
    data class ShareHotel(val hotel: Hotel) : HotelDetailsEventEffect
    data class NavigateToBooking(val hotelId: String) : HotelDetailsEventEffect
    data class NavigateToGallery(val imageList: List<String>) : HotelDetailsEventEffect
    data class ShowAiError(val message: String) : HotelDetailsEventEffect
}
