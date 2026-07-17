package com.softserveacademy.core.domain.model

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
 * @property location User's current location, if available.
 */
data class UserProfile(
    val firstName: String,
    val lastName: String,
    val points: Int,
    val avatarUrl: String,
    val phone: String? = null,
    val age: Int? = null,
    val location: String? = null
) {
    val name: String get() = "$firstName $lastName"
}