package com.softserveacademy.core.data.api

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.HotelBooking
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import kotlinx.serialization.Serializable

interface HotelApiService {
    @GET("hotels/")
    suspend fun getHotels(): List<Hotel>

    @GET("hotels/{id}/")
    suspend fun getHotelById(@Path("id") id: String): Hotel

    @POST("bookings/")
    suspend fun createBooking(
        @Body booking: HotelBooking
    )

    @GET("bookings/")
    suspend fun getAllBookings(): List<HotelBooking>

    @PUT("bookings/{id}/status")
    suspend fun updateBookingStatus(
        @Path("id") bookingId: String,
        @Body request: UpdateBookingStatusRequest
    )
}

@Serializable
data class UpdateBookingStatusRequest(
    val status: String
)
