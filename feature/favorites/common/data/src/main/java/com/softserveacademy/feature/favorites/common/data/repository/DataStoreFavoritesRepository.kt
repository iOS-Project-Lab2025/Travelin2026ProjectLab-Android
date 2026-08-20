package com.softserveacademy.feature.favorites.common.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.softserveacademy.feature.favorites.common.domain.model.FavoriteItem // <-- IMPORT OBLIGATORIO
import com.softserveacademy.feature.favorites.common.domain.repository.FavoritesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favorites_store")

/**
 * Persistent implementation of [FavoritesRepository] using [DataStore].
 * Persists favorite items across app restarts.
 */
@Singleton
class DataStoreFavoritesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : FavoritesRepository {

    override fun getFavorites(): Flow<List<FavoriteItem>> {
        return context.dataStore.data.map { preferences ->
            preferences.asMap().values.mapNotNull { value ->
                try {
                    Json.decodeFromString<FavoriteItem>(value as String)
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    override suspend fun addFavorite(item: FavoriteItem) {
        val json = Json.encodeToString(item)
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(item.id)] = json
        }
    }

    override suspend fun removeFavorite(id: String) {
        context.dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(id))
        }
    }

    override suspend fun isFavorite(id: String): Boolean {
        return context.dataStore.data.map { preferences ->
            preferences.contains(stringPreferencesKey(id))
        }.firstOrNull() ?: false
    }
}