package com.softserveacademy.core.data.api

import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.domain.model.TourBooking
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TourApiService {
    @GET("tours")
    suspend fun getTours(): List<Tour>

    @GET("tours/{id}")
    suspend fun getTourById(@Path("id") id: String): Tour

    @POST("bookings/tours")
    suspend fun createBooking(
        @Body booking: TourBooking
    )

    @GET("bookings/tours")
    suspend fun getAllBookings(): List<TourBooking>

    @PUT("bookings/tours/{id}/status")
    suspend fun updateBookingStatus(
        @Path("id") bookingId: String,
        @Body request: UpdateBookingStatusRequest
    )
}
