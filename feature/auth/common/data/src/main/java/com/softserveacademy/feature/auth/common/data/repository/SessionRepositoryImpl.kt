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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class SessionRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val supabase: SupabaseClient
) : SessionRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        // Sync Supabase session state with local DataStore
        supabase.auth.sessionStatus
            .onEach { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        saveTokens(
                            AuthToken(
                                accessToken = status.session.accessToken,
                                refreshToken = status.session.refreshToken ?: ""
                            )
                        )
                    }
                    else -> {
                        if (status is SessionStatus.NotAuthenticated) {
                            clearTokens()
                        }
                    }
                }
            }
            .launchIn(scope)
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return combine(
            dataStore.data.map { preferences ->
                preferences[ACCESS_TOKEN]?.isNotEmpty() ?: false
            },
            supabase.auth.sessionStatus.map { it is SessionStatus.Authenticated }
        ) { _, supabaseAuthenticated ->
            // Supabase is the single source of truth
            supabaseAuthenticated
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
        return try {
            supabase.auth.signOut()
            clearTokens()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }
}
