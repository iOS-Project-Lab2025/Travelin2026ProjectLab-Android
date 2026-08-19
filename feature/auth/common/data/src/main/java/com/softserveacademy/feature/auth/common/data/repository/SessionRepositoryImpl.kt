package com.softserveacademy.feature.auth.common.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.softserveacademy.feature.auth.common.domain.model.AuthToken
import com.softserveacademy.feature.auth.common.domain.repository.SessionRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SessionRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val supabase: SupabaseClient
) : SessionRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        // Sync Supabase session state with local DataStore
        var wasAuthenticated = false
        supabase.auth.sessionStatus
            .onEach { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        wasAuthenticated = true
                        saveTokens(
                            AuthToken(
                                accessToken = status.session.accessToken,
                                refreshToken = status.session.refreshToken ?: ""
                            )
                        )
                    }
                    is SessionStatus.NotAuthenticated -> {
                        if (wasAuthenticated) {
                            clearTokens()
                            wasAuthenticated = false
                        }
                    }
                    is SessionStatus.Initializing -> {
                        // Wait for initialization to complete
                    }
                    is SessionStatus.RefreshFailure -> {
                        if (wasAuthenticated) {
                            clearTokens()
                            wasAuthenticated = false
                        }
                    }
                }
            }
            .launchIn(scope)

        // Restore session from DataStore on startup and verify
        scope.launch {
            try {
                val prefs = dataStore.data.first()
                val accessToken = prefs[ACCESS_TOKEN]
                val refreshToken = prefs[REFRESH_TOKEN]

                if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
                    supabase.auth.importSession(
                        UserSession(
                            accessToken = accessToken,
                            refreshToken = refreshToken,
                            expiresIn = 3600,
                            tokenType = "bearer",
                            user = null
                        )
                    )
                    // Verify session with server to catch revoked tokens early
                    try {
                        supabase.auth.retrieveUserForCurrentSession(updateSession = true)
                    } catch (_: Exception) {
                        // Verification failed, Supabase will flip to NotAuthenticated
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return combine(
            dataStore.data.map { preferences ->
                !preferences[ACCESS_TOKEN].isNullOrBlank()
            },
            supabase.auth.sessionStatus
        ) { dataStoreAuthenticated, status ->
            when (status) {
                is SessionStatus.Authenticated -> dataStoreAuthenticated
                is SessionStatus.Initializing -> dataStoreAuthenticated
                else -> false
            }
        }.distinctUntilChanged()
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
        // Clear local tokens first to ensure UI responds immediately
        clearTokens()
        return try {
            supabase.auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            // Server-side sign out failed, but local tokens are already cleared
            Result.success(Unit)
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }
}
