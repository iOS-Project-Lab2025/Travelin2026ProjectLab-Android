package com.softserveacademy.feature.booking.hotel.presentation.events

/**
 * Events for the Hotel Contact Information screen.
 */
sealed interface HotelContactInfoEvent {
    data class FirstNameChanged(val firstName: String) : HotelContactInfoEvent
    data class LastNameChanged(val lastName: String) : HotelContactInfoEvent
    data class EmailChanged(val email: String) : HotelContactInfoEvent
    data class PhoneNumberChanged(val phoneNumber: String) : HotelContactInfoEvent
    data class CountryCodeChanged(val countryCode: String) : HotelContactInfoEvent
    data object OnNextClick : HotelContactInfoEvent
    data object OnBackClick : HotelContactInfoEvent
}
