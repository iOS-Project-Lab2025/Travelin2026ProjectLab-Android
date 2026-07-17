package com.softserveacademy.core.domain.model

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