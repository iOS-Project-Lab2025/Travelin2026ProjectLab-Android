package com.softserveacademy.feature.booking.common.presentation.events

/**
 * Events for the Booking Contact Information screen.
 */
sealed interface TravelBookingContactInfoEvent {
    data class FirstNameChanged(val firstName: String) : TravelBookingContactInfoEvent
    data class LastNameChanged(val lastName: String) : TravelBookingContactInfoEvent
    data class EmailChanged(val email: String) : TravelBookingContactInfoEvent
    data class PhoneNumberChanged(val phoneNumber: String) : TravelBookingContactInfoEvent
    data class CountryCodeChanged(val countryCode: String) : TravelBookingContactInfoEvent
    data object OnNextClick : TravelBookingContactInfoEvent
    data object OnBackClick : TravelBookingContactInfoEvent
}