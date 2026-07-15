package com.softserveacademy.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.softserveacademy.core.domain.model.AppTheme
import com.softserveacademy.core.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [ThemeRepository] using [DataStore] for persistence.
 */
class ThemeRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ThemeRepository {

    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    override fun getTheme(): Flow<AppTheme> {
        return dataStore.data.map { preferences ->
            val themeName = preferences[PreferencesKeys.THEME_MODE] ?: AppTheme.SYSTEM.name
            try {
                AppTheme.valueOf(themeName)
            } catch (e: IllegalArgumentException) {
                AppTheme.SYSTEM
            }
        }
    }

    override suspend fun setTheme(theme: AppTheme) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = theme.name
        }
    }
}
