package com.softserveacademy.core.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for global application preferences.
 */
interface CorePreferencesRepository {
    /**
     * Checks if the user is entering the app for the first time.
     * @return A Flow of Boolean (true if it's the first time).
     */
    fun isFirstTimeUser(): Flow<Boolean>

    /**
     * Updates the first time user status.
     * @param isFirstTime False when the user completes or skips onboarding.
     */
    suspend fun setFirstTimeUser(isFirstTime: Boolean)
}