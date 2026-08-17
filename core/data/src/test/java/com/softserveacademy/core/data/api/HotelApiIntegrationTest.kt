package com.softserveacademy.core.data.api

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class HotelApiIntegrationTest {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        prettyPrint = true
    }

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        println("API_LOG: $message")
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8000/") // Adjust if your backend is on a different port/host
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val apiService = retrofit.create(HotelApiService::class.java)

    @Test
    fun fetchBookingsFromApi() = runBlocking {
        println("--- Starting API Call ---")
        try {
            val bookings = apiService.getAllBookings()
            println("--- Response Received ---")
            println("Number of bookings: ${bookings.size}")
            bookings.forEachIndexed { index, booking ->
                println("Booking [$index]: $booking")
            }
        } catch (e: Exception) {
            println("--- API Call Failed ---")
            e.printStackTrace()
        }
    }
}
