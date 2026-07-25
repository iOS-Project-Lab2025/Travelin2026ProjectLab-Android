package com.softserveacademy.feature.auth.common.domain.usecase

import com.softserveacademy.feature.auth.common.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow

class CheckSessionUseCase(
    private val repository: SessionRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.isLoggedIn()
    }
}
