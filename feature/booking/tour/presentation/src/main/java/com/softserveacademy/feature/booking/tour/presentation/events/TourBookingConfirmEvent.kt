package com.softserveacademy.feature.booking.tour.presentation.events

sealed interface TourBookingConfirmEvent {
    data object OnConfirmClick : TourBookingConfirmEvent
    data object OnBackClick : TourBookingConfirmEvent
    data object OnRetryClick : TourBookingConfirmEvent
    data object OnDismissError : TourBookingConfirmEvent
    data object OnPaymentSuccess : TourBookingConfirmEvent
    data object OnPaymentReset : TourBookingConfirmEvent
    data object OnSimulateSuccessClick : TourBookingConfirmEvent
    data object OnSimulateFailureClick : TourBookingConfirmEvent
    data object OnDismissPaymentSimulationSheet : TourBookingConfirmEvent
}
