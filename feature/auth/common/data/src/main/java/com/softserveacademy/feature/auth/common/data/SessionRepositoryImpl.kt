package com.softserveacademy.feature.auth.common.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.softserveacademy.feature.auth.common.domain.AuthToken
import com.softserveacademy.feature.auth.common.domain.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SessionRepository {

    override fun isLoggedIn(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]?.isNotEmpty() ?: false
        }
    }

    override fun getAccessToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }
    }

    override fun getRefreshToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN]
        }
    }

    override suspend fun saveTokens(token: AuthToken) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token.accessToken
            preferences[REFRESH_TOKEN] = token.refreshToken
        }
    }

    override suspend fun clearTokens(): Result<Unit> {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
        }
        return Result.success(Unit)
    }

    override suspend fun logout(): Result<Unit> {
        return clearTokens()
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }
}
