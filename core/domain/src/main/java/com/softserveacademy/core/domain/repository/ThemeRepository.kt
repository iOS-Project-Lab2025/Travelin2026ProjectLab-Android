package com.softserveacademy.core.domain.repository

import com.softserveacademy.core.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing application theme preferences.
 */
interface ThemeRepository {
    /**
     * Returns a [Flow] of the current [AppTheme] preference.
     */
    fun getTheme(): Flow<AppTheme>

    /**
     * Updates the application theme preference.
     * @param theme The new [AppTheme] to set.
     */
    suspend fun setTheme(theme: AppTheme)
}
