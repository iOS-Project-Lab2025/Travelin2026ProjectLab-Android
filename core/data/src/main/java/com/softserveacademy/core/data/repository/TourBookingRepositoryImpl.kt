package com.softserveacademy.core.data.repository

import android.util.Log
import com.softserveacademy.core.data.api.TourApiService
import com.softserveacademy.core.data.api.UpdateBookingStatusRequest
import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.model.TourBooking
import com.softserveacademy.core.domain.repository.TourBookingRepository
import com.softserveacademy.core.error.extension.map
import com.softserveacademy.core.error.mapper.ExceptionMapper
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.core.error.util.safeCall
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TourBookingRepositoryImpl @Inject constructor(
    private val tourApiService: TourApiService,
    private val mapper: ExceptionMapper
) : TourBookingRepository {

    override suspend fun saveBooking(booking: TourBooking): AppResult<Unit> = safeCall(mapper) {
        // Send to remote API
        try {
            val bookingJson = Json.encodeToString(booking)
            Log.d("HotelBookingRepo", "Sending Booking JSON: $bookingJson")
            tourApiService.createBooking(booking)
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("HotelBookingRepo", "Remote createBooking failed with ${e.code()}: $errorBody")
            } else {
                Log.e("HotelBookingRepo", "Remote createBooking failed: ${e.message}", e)
            }
            throw e
        }
    }

    override suspend fun getBookings(): AppResult<List<TourBooking>> = safeCall(mapper) {
        tourApiService.getAllBookings()
    }

    override suspend fun getBookingById(bookingId: String): AppResult<TourBooking?> {
        return getBookings().map { bookings ->
            bookings.find { it.bookingId == bookingId }
        }
    }

    override suspend fun updateBookingStatus(bookingId: String, status: BookingStatus): AppResult<Unit> = safeCall(mapper) {
        tourApiService.updateBookingStatus(
            bookingId = bookingId,
            request = UpdateBookingStatusRequest(status = status.name)
        )
    }
}
