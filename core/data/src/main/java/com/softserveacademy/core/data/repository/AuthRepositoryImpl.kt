package com.softserveacademy.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.softserveacademy.core.domain.model.User
import com.softserveacademy.core.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Duration.Companion.milliseconds

class AuthRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : AuthRepository {

    override suspend fun register(user: User, password: String): Result<Unit> {
        // Mocking a network call
        delay(2000.milliseconds)
        return if (user.email.contains("@")) {
            saveSession()
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid email format"))
        }
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        // Mocking a network call
        delay(2000.milliseconds)
        return if (email.contains("@") && password.length >= 6) {
            saveSession()
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }

    override suspend fun recoverPassword(email: String): Result<Unit> {
        // Mocking a network call
        delay(2000.milliseconds)
        return if (email.contains("@")) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("User not found"))
        }
    }

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

    private suspend fun saveSession() {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
        }
    }

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }
}
