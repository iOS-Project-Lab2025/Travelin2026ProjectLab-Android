package com.softserveacademy.core.domain.model

data class User (
    val firstName: String,
    val lastName: String,
    val phone: String,
    val age: Int,
    val email: String

    // Maybe I should add a createdAt attr also.
    // Shouldn't we store the pass hash here also?
) {
}