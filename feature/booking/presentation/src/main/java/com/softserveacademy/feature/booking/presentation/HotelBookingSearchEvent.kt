package com.softserveacademy.feature.booking.presentation

/**
 * Sealed interface representing UI events for the hotel booking search screen.
 */
sealed interface HotelBookingSearchEvent {
    data class OnDateRangeSelected(val startDateMillis: Long?, val endDateMillis: Long?) : HotelBookingSearchEvent
    data class OnAdultsCountChange(val count: Int) : HotelBookingSearchEvent
    data class OnChildrenCountChange(val count: Int) : HotelBookingSearchEvent
    data class OnHasPetsChange(val hasPets: Boolean) : HotelBookingSearchEvent
    data object OnNextClick : HotelBookingSearchEvent
    data object OnDismissGuestBottomSheet : HotelBookingSearchEvent
    data object OnAcceptGuests : HotelBookingSearchEvent
}
