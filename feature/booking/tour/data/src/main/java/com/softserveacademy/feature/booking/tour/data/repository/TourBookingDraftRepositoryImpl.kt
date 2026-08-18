package com.softserveacademy.feature.booking.tour.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.softserveacademy.feature.booking.tour.domain.model.TourBookingDraft
import com.softserveacademy.feature.booking.tour.domain.repository.TourBookingDraftRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tour_booking_drafts")

@Singleton
class TourBookingDraftRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : TourBookingDraftRepository {

    override suspend fun saveDraft(draft: TourBookingDraft) {
        val tourId = draft.tourId ?: return
        val json = Json.encodeToString(draft)
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(tourId)] = json
        }
    }

    override suspend fun getDraft(tourId: String): TourBookingDraft? {
        val json = context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(tourId)]
        }.firstOrNull() ?: return null

        return try {
            Json.decodeFromString<TourBookingDraft>(json)
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun clearDraft(tourId: String) {
        context.dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(tourId))
        }
    }
}
