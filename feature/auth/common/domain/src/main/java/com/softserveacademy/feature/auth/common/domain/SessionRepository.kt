package com.softserveacademy.feature.auth.common.domain

import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun isLoggedIn(): Flow<Boolean>
    fun getAccessToken(): Flow<String?>
    fun getRefreshToken(): Flow<String?>
    suspend fun saveTokens(token: AuthToken)
    suspend fun clearTokens(): Result<Unit>
    suspend fun logout(): Result<Unit>
}
