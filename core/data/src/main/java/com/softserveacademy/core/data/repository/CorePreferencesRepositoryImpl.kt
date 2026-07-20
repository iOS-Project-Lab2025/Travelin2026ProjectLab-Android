package com.softserveacademy.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.softserveacademy.core.domain.repository.CorePreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [CorePreferencesRepository] using Jetpack DataStore.
 */
class CorePreferencesRepositoryImpl @Inject constructor(
        private val dataStore: DataStore<Preferences>
) : CorePreferencesRepository {

    private object Keys {
        val IS_FIRST_TIME = booleanPreferencesKey("is_first_time_user")
    }

    override fun isFirstTimeUser(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
                preferences[Keys.IS_FIRST_TIME] ?: true
        }
    }

    override suspend fun setFirstTimeUser(isFirstTime: Boolean) {
        dataStore.edit { preferences ->
                preferences[Keys.IS_FIRST_TIME] = isFirstTime
        }
    }
}