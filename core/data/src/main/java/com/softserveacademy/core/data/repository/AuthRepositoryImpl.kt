package com.softserveacademy.core.data.repository

import com.softserveacademy.core.domain.model.User
import com.softserveacademy.core.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class AuthRepositoryImpl : AuthRepository {
    override suspend fun register(user: User, password: String): Result<Unit> {
        // Mocking a network call
        delay(2000.milliseconds)
        return if (user.email.contains("@")) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid email format"))
        }
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        // Mocking a network call
        delay(2000.milliseconds)
        return if (email.contains("@") && password.length >= 6) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }
}
