package com.softserveacademy.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.softserveacademy.core.data.api.HotelApiService
import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.model.HotelBooking
import com.softserveacademy.core.domain.repository.HotelBookingRepository
import com.softserveacademy.core.error.extension.map
import com.softserveacademy.core.error.mapper.ExceptionMapper
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.core.error.util.safeCall
import com.softserveacademy.core.error.util.safeFlow
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
    @ApplicationContext private val context: Context,
    private val hotelApiService: HotelApiService,
    private val mapper: ExceptionMapper
) : HotelBookingRepository {

    private val bookingsKey = stringPreferencesKey("hotel_bookings")

    override suspend fun saveBooking(booking: HotelBooking): AppResult<Unit> = safeCall(mapper) {
        // Send to remote API
        try {
            val bookingJson = Json.encodeToString(booking)
            android.util.Log.d("HotelBookingRepo", "Sending Booking JSON: $bookingJson")
            hotelApiService.createBooking(booking)
        } catch (e: Exception) {
            if (e is retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                android.util.Log.e("HotelBookingRepo", "Remote createBooking failed with ${e.code()}: $errorBody")
            } else {
                android.util.Log.e("HotelBookingRepo", "Remote createBooking failed: ${e.message}", e)
            }
            throw e
        }
        
        // Also save to local DataStore as a cache
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

    override fun getBookings(): Flow<AppResult<List<HotelBooking>>> {
        return context.hotelBookingDataStore.data
            .map { preferences -> getBookingsList(preferences) }
            .safeFlow(mapper)
    }

    override fun getBookingById(bookingId: String): Flow<AppResult<HotelBooking?>> {
        return getBookings().map { result ->
            result.map { list -> list.find { it.bookingId == bookingId } }
        }
    }

    override suspend fun getRemoteBookings(): AppResult<List<HotelBooking>> = safeCall(mapper) {
        hotelApiService.getAllBookings()
    }

    override suspend fun updateBookingStatus(bookingId: String, status: BookingStatus): AppResult<Unit> = safeCall(mapper) {
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
