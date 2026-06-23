package com.softserveacademy.core.domain.usecase

import com.softserveacademy.core.domain.repository.AuthRepository

class RecoverPasswordUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        if (email.isBlank() || !email.contains("@")) {
            return Result.failure(IllegalArgumentException("Invalid email address"))
        }
        return repository.recoverPassword(email)
    }
}
