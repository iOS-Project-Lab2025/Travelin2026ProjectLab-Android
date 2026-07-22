package com.softserveacademy.feature.booking.common.presentation.events

/**
 * Sealed interface representing UI events for a generic enter booking details screen.
 */
sealed interface TravelEnterBookingDetailsEvent {
    data class OnDateRangeSelected(val startDateMillis: Long?, val endDateMillis: Long?) : TravelEnterBookingDetailsEvent
    data class OnAdultsCountChange(val count: Int) : TravelEnterBookingDetailsEvent
    data class OnChildrenCountChange(val count: Int) : TravelEnterBookingDetailsEvent
    data class OnHasPetsChange(val hasPets: Boolean) : TravelEnterBookingDetailsEvent
    data object OnNextClick : TravelEnterBookingDetailsEvent
    data object OnBackClick : TravelEnterBookingDetailsEvent
    data object OnDismissBottomSheet : TravelEnterBookingDetailsEvent
    data object OnAcceptClick : TravelEnterBookingDetailsEvent
}
