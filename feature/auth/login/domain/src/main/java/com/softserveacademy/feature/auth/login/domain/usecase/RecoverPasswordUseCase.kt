package com.softserveacademy.feature.auth.login.domain.usecase

import com.softserveacademy.feature.auth.login.domain.repository.LoginRepository

class RecoverPasswordUseCase(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        if (email.isBlank() || !email.contains("@")) {
            return Result.failure(IllegalArgumentException("Invalid email address"))
        }
        return repository.recoverPassword(email)
    }
}
