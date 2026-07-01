package com.softserveacademy.feature.auth.common.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.softserveacademy.feature.auth.common.domain.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SessionRepository {

    override fun isLoggedIn(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }
    }

    override suspend fun logout(): Result<Unit> {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
        }
        return Result.success(Unit)
    }

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }
}
