package com.softserveacademy.feature.booking.hotel.presentation.events

/**
 * Events for the Hotel Booking Confirmation screen.
 */
sealed interface HotelBookingConfirmEvent {
    data object OnConfirmClick : HotelBookingConfirmEvent
    data object OnBackClick : HotelBookingConfirmEvent
}
