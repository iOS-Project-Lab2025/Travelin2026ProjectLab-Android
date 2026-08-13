package com.softserveacademy.core.data.di

import com.softserveacademy.core.data.api.GoogleMapsApiService
import com.softserveacademy.core.data.api.HotelApiService
import com.softserveacademy.core.data.api.TourApiService
import com.softserveacademy.core.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("MockRetrofit")
    fun provideRetrofit(json: Json, okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        val factory = json.asConverterFactory(contentType)
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            //.baseUrl("https://travelin2026projectlab-android-api.onrender.com/")
            .client(okHttpClient)
            .addConverterFactory(factory)
            .build()
    }

    @Provides
    @Singleton
    @Named("GoogleRetrofit")
    fun provideGoogleRetrofit(json: Json, okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        val factory = json.asConverterFactory(contentType)
        return Retrofit.Builder()
            .baseUrl("https://routes.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(factory)
            .build()
    }

    @Provides
    @Singleton
    fun provideHotelApiService(@Named("MockRetrofit") retrofit: Retrofit): HotelApiService {
        return retrofit.create(HotelApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTourApiService(@Named("MockRetrofit") retrofit: Retrofit): TourApiService {
        return retrofit.create(TourApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGoogleMapsApiService(@Named("GoogleRetrofit") retrofit: Retrofit): GoogleMapsApiService {
        return retrofit.create(GoogleMapsApiService::class.java)
    }
}
