package com.softserveacademy.feature.auth.login.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.softserveacademy.feature.auth.login.domain.LoginRepository
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class LoginRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : LoginRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        delay(2000.milliseconds)
        return if (email.contains("@") && password.length >= 6) {
            saveSession()
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }

    override suspend fun recoverPassword(email: String): Result<Unit> {
        delay(2000.milliseconds)
        return if (email.contains("@")) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("User not found"))
        }
    }

    private suspend fun saveSession() {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = "mock_access_token"
            preferences[REFRESH_TOKEN] = "mock_refresh_token"
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }
}
