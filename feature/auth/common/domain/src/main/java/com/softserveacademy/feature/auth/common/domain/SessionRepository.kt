package com.softserveacademy.feature.auth.common.domain

import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun isLoggedIn(): Flow<Boolean>
    suspend fun logout(): Result<Unit>
}
