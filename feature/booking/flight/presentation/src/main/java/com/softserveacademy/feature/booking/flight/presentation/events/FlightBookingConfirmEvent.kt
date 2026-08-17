package com.softserveacademy.feature.booking.flight.presentation.events

sealed interface FlightBookingConfirmEvent {
    object OnConfirmClick : FlightBookingConfirmEvent
    object OnPaymentSuccess : FlightBookingConfirmEvent
    object OnPaymentReset : FlightBookingConfirmEvent
    object OnSimulateSuccessClick : FlightBookingConfirmEvent
    object OnSimulateFailureClick : FlightBookingConfirmEvent
    object OnDismissPaymentSimulationSheet : FlightBookingConfirmEvent
    object OnRetryClick : FlightBookingConfirmEvent
    object OnDismissError : FlightBookingConfirmEvent
}