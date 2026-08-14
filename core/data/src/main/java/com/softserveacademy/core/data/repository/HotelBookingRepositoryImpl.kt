package com.softserveacademy.core.data.repository

import com.softserveacademy.core.data.api.HotelApiService
import com.softserveacademy.core.data.api.UpdateBookingStatusRequest
import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.model.HotelBooking
import com.softserveacademy.core.domain.repository.HotelBookingRepository
import com.softserveacademy.core.error.extension.map
import com.softserveacademy.core.error.mapper.ExceptionMapper
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.core.error.util.safeCall
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HotelBookingRepositoryImpl @Inject constructor(
    private val hotelApiService: HotelApiService,
    private val mapper: ExceptionMapper
) : HotelBookingRepository {

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
    }

    override suspend fun getBookings(): AppResult<List<HotelBooking>> = safeCall(mapper) {
        hotelApiService.getAllBookings()
    }

    override suspend fun getBookingById(bookingId: String): AppResult<HotelBooking?> {
        return getBookings().map { bookings ->
            bookings.find { it.bookingId == bookingId }
        }
    }

    override suspend fun updateBookingStatus(bookingId: String, status: BookingStatus): AppResult<Unit> = safeCall(mapper) {
        hotelApiService.updateBookingStatus(
            bookingId = bookingId,
            request = UpdateBookingStatusRequest(status = status.name)
        )
    }
}
