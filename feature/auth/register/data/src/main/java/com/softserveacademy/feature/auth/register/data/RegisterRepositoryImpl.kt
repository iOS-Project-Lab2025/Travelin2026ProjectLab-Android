package com.softserveacademy.feature.auth.register.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.softserveacademy.core.domain.model.User
import com.softserveacademy.feature.auth.register.domain.RegisterRepository
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class RegisterRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : RegisterRepository {

    override suspend fun register(user: User, password: String): Result<Unit> {
        delay(2000.milliseconds)
        return if (user.email.contains("@")) {
            saveSession()
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid email format"))
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
