package com.softserveacademy.profile.domain.model

/**
 * Represents the summary information displayed for a user's profile.
 *
 * A user profile contains the essential information presented throughout
 * the application, such as the user's display name, loyalty points,
 * profile image, and current location.
 *
 * @property name User's display name.
 * @property points User's accumulated loyalty or reward points.
 * @property avatarUrl URL of the user's profile image.
 * @property phone User's contact phone number.
 * @property birthDate User's birth date as a timestamp.
 * @property isBirthDateChanged Flag indicating if the birth date has been modified post-registration.
 * @property location User's current location, if available.
 */
data class UserProfile(
    val firstName: String,
    val lastName: String,
    val points: Int,
    val avatarUrl: String,
    val phone: String? = null,
    val birthDate: Long? = null,
    val isBirthDateChanged: Boolean = false,
    val location: String? = null
) {
    val name: String get() = "$firstName $lastName"
}
