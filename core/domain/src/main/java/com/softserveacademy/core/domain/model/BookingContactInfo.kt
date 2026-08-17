package com.softserveacademy.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serializable as JavaSerializable

/**
 * Data class representing the contact information for a booking.
 *
 *  @property firstName The first name of the guest or participant.
 *  @property lastName The last name of the guest or participant.
 *  @property email The email address of the guest or participant.
 *  @property countryCode The country code of the guest's phone number.
 *  @property phoneNumber The phone number of the guest or participant.
 */
@Serializable
data class BookingContactInfo(
    @SerialName("first_name")
    val firstName: String = "",
    @SerialName("last_name")
    val lastName: String = "",
    val email: String = "",
    @SerialName("country_code")
    val countryCode: String = "",
    @SerialName("phone_number")
    val phoneNumber: String = ""
) : JavaSerializable
