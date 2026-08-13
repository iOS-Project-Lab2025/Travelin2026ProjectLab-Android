package com.softserveacademy.core.data.api

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.HotelBooking
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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
}
