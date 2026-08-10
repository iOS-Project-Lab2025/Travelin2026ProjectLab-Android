package com.softserveacademy.feature.booking.hotel.presentation.events

/**
 * Events for the Hotel Booking Confirmation screen.
 */
sealed interface HotelBookingConfirmEvent {
    data object OnConfirmClick : HotelBookingConfirmEvent
    data object OnBackClick : HotelBookingConfirmEvent
    data object OnPaymentSuccess : HotelBookingConfirmEvent
    data object OnPaymentReset : HotelBookingConfirmEvent
    data object OnSimulateSuccessClick : HotelBookingConfirmEvent
    data object OnSimulateFailureClick : HotelBookingConfirmEvent
    data object OnDismissPaymentSimulationSheet : HotelBookingConfirmEvent
    data object OnRetryClick : HotelBookingConfirmEvent
    data object OnDismissError : HotelBookingConfirmEvent
}
