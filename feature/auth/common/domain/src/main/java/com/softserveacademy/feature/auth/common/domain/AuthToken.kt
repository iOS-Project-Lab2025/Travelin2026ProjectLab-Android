package com.softserveacademy.feature.auth.common.domain

data class AuthToken(
    val accessToken: String,
    val refreshToken: String
)
