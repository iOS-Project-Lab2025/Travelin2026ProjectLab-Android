package com.softserveacademy.feature.booking.common.presentation.states

import java.io.Serializable

/**
 * State class for the Booking Contact Information screen.
 */
data class TravelBookingContactInfoState(
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val countryCode: String = "+855",
    val phoneNumber: String = "",
    val phoneNumberError: String? = null
) : Serializable