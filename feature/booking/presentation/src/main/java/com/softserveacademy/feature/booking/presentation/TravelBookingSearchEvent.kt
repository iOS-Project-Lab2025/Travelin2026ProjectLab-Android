package com.softserveacademy.feature.booking.presentation

/**
 * Sealed interface representing UI events for a generic booking search screen.
 */
sealed interface TravelBookingSearchEvent {
    data class OnDateRangeSelected(val startDateMillis: Long?, val endDateMillis: Long?) : TravelBookingSearchEvent
    data class OnAdultsCountChange(val count: Int) : TravelBookingSearchEvent
    data class OnChildrenCountChange(val count: Int) : TravelBookingSearchEvent
    data class OnHasPetsChange(val hasPets: Boolean) : TravelBookingSearchEvent
    data object OnNextClick : TravelBookingSearchEvent
    data object OnBackClick : TravelBookingSearchEvent
    data object OnDismissGuestBottomSheet : TravelBookingSearchEvent
    data object OnAcceptGuests : TravelBookingSearchEvent
}
