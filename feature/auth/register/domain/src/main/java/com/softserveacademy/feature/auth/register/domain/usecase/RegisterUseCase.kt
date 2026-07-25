package com.softserveacademy.feature.auth.register.domain.usecase

import com.softserveacademy.core.domain.model.User
import com.softserveacademy.feature.auth.register.domain.repository.RegisterRepository

class RegisterUseCase(
    private val repository: RegisterRepository
) {
    suspend operator fun invoke(user: User, password: String): Result<Unit> {
        // Here you can add domain-level validation if needed
        if (user.email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password cannot be empty"))
        }
        return repository.register(user, password)
    }
}
