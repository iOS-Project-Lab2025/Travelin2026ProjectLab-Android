package com.softserveacademy.feature.auth.login.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
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
            preferences[IS_LOGGED_IN] = true
        }
    }

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }
}
