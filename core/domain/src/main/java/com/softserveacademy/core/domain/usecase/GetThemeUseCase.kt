package com.softserveacademy.core.domain.usecase

import com.softserveacademy.core.domain.model.AppTheme
import com.softserveacademy.core.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to retrieve the current application theme preference.
 */
class GetThemeUseCase(
    private val repository: ThemeRepository
) {
    operator fun invoke(): Flow<AppTheme> = repository.getTheme()
}
