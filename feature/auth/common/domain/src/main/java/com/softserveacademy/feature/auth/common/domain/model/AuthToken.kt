package com.softserveacademy.feature.auth.common.domain.model

data class AuthToken(
    val accessToken: String,
    val refreshToken: String
)
