package com.softserveacademy.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.model.HotelBooking
import com.softserveacademy.core.domain.repository.HotelBookingRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.hotelBookingDataStore: DataStore<Preferences> by preferencesDataStore(name = "hotel_bookings")

@Singleton
class HotelBookingRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : HotelBookingRepository {

    private val bookingsKey = stringPreferencesKey("hotel_bookings")

    override suspend fun saveBooking(booking: HotelBooking) {
        context.hotelBookingDataStore.edit { preferences ->
            val currentBookings = getBookingsList(preferences)
            val updatedBookings = if (currentBookings.any { it.bookingId == booking.bookingId }) {
                currentBookings.map { if (it.bookingId == booking.bookingId) booking else it }
            } else {
                currentBookings + booking
            }
            preferences[bookingsKey] = Json.encodeToString(updatedBookings)
        }
    }

    override fun getBookings(): Flow<List<HotelBooking>> {
        return context.hotelBookingDataStore.data.map { preferences ->
            getBookingsList(preferences)
        }
    }

    override fun getBookingById(bookingId: String): Flow<HotelBooking?> {
        return getBookings().map { list ->
            list.find { it.bookingId == bookingId }
        }
    }

    override suspend fun updateBookingStatus(bookingId: String, status: BookingStatus) {
        context.hotelBookingDataStore.edit { preferences ->
            val currentBookings = getBookingsList(preferences)
            val updatedBookings = currentBookings.map {
                if (it.bookingId == bookingId) it.copy(status = status) else it
            }
            preferences[bookingsKey] = Json.encodeToString(updatedBookings)
        }
    }

    private fun getBookingsList(preferences: Preferences): List<HotelBooking> {
        val json = preferences[bookingsKey] ?: return emptyList()
        return try {
            Json.decodeFromString<List<HotelBooking>>(json)
        } catch (_: Exception) {
            emptyList()
        }
    }
}
