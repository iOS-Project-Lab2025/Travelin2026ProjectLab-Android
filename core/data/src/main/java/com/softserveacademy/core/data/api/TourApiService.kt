package com.softserveacademy.core.data.api

import com.softserveacademy.core.domain.model.Tour
import retrofit2.http.GET
import retrofit2.http.Path

interface TourApiService {
    @GET("tours")
    suspend fun getTours(): List<Tour>

    @GET("tours/{id}")
    suspend fun getTourById(@Path("id") id: String): Tour
}
