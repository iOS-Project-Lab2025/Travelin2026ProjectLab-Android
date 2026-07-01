package com.softserveacademy.feature.auth.register.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
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
            preferences[IS_LOGGED_IN] = true
        }
    }

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }
}
