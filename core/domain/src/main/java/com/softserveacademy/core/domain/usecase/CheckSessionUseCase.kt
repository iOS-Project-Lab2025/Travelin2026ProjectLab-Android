package com.softserveacademy.core.domain.usecase

import com.softserveacademy.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class CheckSessionUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.isLoggedIn()
    }
}
