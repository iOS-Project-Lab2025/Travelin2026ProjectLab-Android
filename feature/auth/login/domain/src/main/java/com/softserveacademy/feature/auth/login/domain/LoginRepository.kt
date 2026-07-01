package com.softserveacademy.feature.auth.login.domain

interface LoginRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun recoverPassword(email: String): Result<Unit>
}
