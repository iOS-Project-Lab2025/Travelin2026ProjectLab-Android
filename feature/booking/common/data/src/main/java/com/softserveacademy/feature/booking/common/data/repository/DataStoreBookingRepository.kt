package com.softserveacademy.feature.booking.common.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.softserveacademy.feature.booking.common.domain.model.HotelBookingDraft
import com.softserveacademy.feature.booking.common.domain.repository.BookingRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "booking_drafts")

/**
 * Persistent implementation of [BookingRepository] using [DataStore].
 * Persists data across app restarts.
 */
@Singleton
class DataStoreBookingRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : BookingRepository {

    override suspend fun saveHotelBookingDraft(draft: HotelBookingDraft) {
        val hotelId = draft.hotelId ?: return
        val json = Json.encodeToString(draft)
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(hotelId)] = json
        }
    }

    override suspend fun getHotelBookingDraft(hotelId: String): HotelBookingDraft? {
        val json = context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(hotelId)]
        }.firstOrNull() ?: return null

        return try {
            Json.decodeFromString<HotelBookingDraft>(json)
        } catch (e: Exception) {
            null
        }
    }
}
