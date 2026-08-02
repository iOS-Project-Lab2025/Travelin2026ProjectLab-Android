package com.softserveacademy.home.presentation.events

import com.softserveacademy.core.domain.model.HotelDetails

/**
 * On click effects that happens one time for the Hotel Detail screen.
 */
sealed interface HotelDetailsEventEffect {
    data object NavigateBack : HotelDetailsEventEffect
    data class ShareHotel(val hotel: HotelDetails) : HotelDetailsEventEffect
    data class NavigateToBooking(val hotelId: Int) : HotelDetailsEventEffect
    data class NavigateToGallery(val imageList: List<String>) : HotelDetailsEventEffect
}
