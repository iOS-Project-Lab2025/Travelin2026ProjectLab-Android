package com.softserveacademy.core.domain.model

/**
 * Represents a user of the application.
 *
 * A user contains the personal information required to identify and
 * communicate with the traveler.
 *
 * @property firstName User's first name.
 * @property lastName User's last name.
 * @property phone User's contact phone number.
 * @property birthDate User's birth date as a timestamp.
 * @property email User's email address.
 */
data class User (
    val firstName: String,
    val lastName: String,
    val phone: String,
    val birthDate: Long,
    val email: String

) {
}