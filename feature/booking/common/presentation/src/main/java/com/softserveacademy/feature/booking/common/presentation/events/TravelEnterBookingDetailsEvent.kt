package com.softserveacademy.feature.booking.common.presentation.events

/**
 * Sealed interface representing common UI events for an enter booking details screen.
 * Holds events shared across different booking types, like date selection and navigation.
 */
sealed interface TravelEnterBookingDetailsEvent {
    data class OnDateRangeSelected(val startDateMillis: Long?, val endDateMillis: Long?) : TravelEnterBookingDetailsEvent
    data object OnNextClick : TravelEnterBookingDetailsEvent
    data object OnBackClick : TravelEnterBookingDetailsEvent
    data object OnDismissBottomSheet : TravelEnterBookingDetailsEvent
    data object OnAcceptClick : TravelEnterBookingDetailsEvent
}
