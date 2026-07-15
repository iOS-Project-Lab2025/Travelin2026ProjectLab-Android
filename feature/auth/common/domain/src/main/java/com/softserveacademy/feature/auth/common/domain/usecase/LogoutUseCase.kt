package com.softserveacademy.feature.auth.common.domain.usecase

import com.softserveacademy.feature.auth.common.domain.SessionRepository

/**
 * Use case to handle user logout.
 * Clears the user session and authentication tokens.
 */
class LogoutUseCase(
    private val repository: SessionRepository,
) {
    /**
     * Executes the logout process.
     * @return Result indicating success or failure of the logout operation.
     */
    suspend operator fun invoke(): Result<Unit> {
        return repository.logout()
    }
}
