package com.softserveacademy.feature.booking.hotel.presentation.events

/**
 * Events for the Hotel Booking Confirmation screen.
 */
sealed interface HotelBookingConfirmEvent {
    /**
     * Event triggered when the user clicks the "Book Now" button.
     */
    data object OnBookNowClick : HotelBookingConfirmEvent

    /**
     * Event triggered when the user clicks the back button.
     */
    data object OnBackClick : HotelBookingConfirmEvent
}
