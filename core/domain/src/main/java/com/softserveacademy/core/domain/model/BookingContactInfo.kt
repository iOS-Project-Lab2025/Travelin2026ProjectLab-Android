package com.softserveacademy.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serializable as JavaSerializable

/**
 * Generic model for contact information across all booking types (Hotel, Flight, Tours).
 *
 * @property firstName Contact person's first name.
 * @property lastName Contact person's last name.
 * @property email Primary communication email.
 * @property countryCode International dial code (e.g., +1, +56).
 * @property phoneNumber Primary contact number.
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
