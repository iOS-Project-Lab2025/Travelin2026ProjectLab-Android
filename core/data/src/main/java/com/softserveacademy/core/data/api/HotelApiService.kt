package com.softserveacademy.core.data.api

import com.softserveacademy.core.domain.model.Hotel
import retrofit2.http.GET
import retrofit2.http.Path

interface HotelApiService {
    @GET("hotels")
    suspend fun getHotels(): List<Hotel>

    @GET("hotels/{id}")
    suspend fun getHotelById(@Path("id") id: String): Hotel
}
