package com.softserveacademy.home.presentation.events

import com.softserveacademy.core.domain.model.DestinationDetails

/**
 * On click effects that happens one time for the Hotel Detail screen.
 */
sealed interface HotelDetailsEventEffect {
    data object NavigateBack : HotelDetailsEventEffect
    data class ShareHotel(val hotel: DestinationDetails) : HotelDetailsEventEffect
    data class NavigateToBooking(val hotelId: String) : HotelDetailsEventEffect
    data class NavigateToGallery(val imageList: List<String>) : HotelDetailsEventEffect
}
