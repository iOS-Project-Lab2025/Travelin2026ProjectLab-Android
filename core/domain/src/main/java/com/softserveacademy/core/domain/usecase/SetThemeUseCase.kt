package com.softserveacademy.core.domain.usecase

import com.softserveacademy.core.domain.model.AppTheme
import com.softserveacademy.core.domain.repository.ThemeRepository

/**
 * Use case to update the application theme preference.
 */
class SetThemeUseCase(
    private val repository: ThemeRepository
) {
    suspend operator fun invoke(theme: AppTheme) = repository.setTheme(theme)
}
