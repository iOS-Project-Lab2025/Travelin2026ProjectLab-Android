package com.softserveacademy.feature.booking.flight.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.softserveacademy.feature.booking.flight.domain.model.FlightBookingDraft
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingDraftRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "flight_booking_prefs")

@Singleton
class FlightBookingDraftRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FlightBookingDraftRepository {

    private val draftKey = stringPreferencesKey("current_flight_draft")

    override suspend fun saveDraft(draft: FlightBookingDraft) {
        context.dataStore.edit { prefs ->
            prefs[draftKey] = Json.encodeToString(draft)
        }
    }

    override fun getDraft(): Flow<FlightBookingDraft?> = context.dataStore.data.map { prefs ->
        prefs[draftKey]?.let { Json.decodeFromString<FlightBookingDraft>(it) }
    }

    override suspend fun clearDraft() {
        context.dataStore.edit { it.remove(draftKey) }
    }
}