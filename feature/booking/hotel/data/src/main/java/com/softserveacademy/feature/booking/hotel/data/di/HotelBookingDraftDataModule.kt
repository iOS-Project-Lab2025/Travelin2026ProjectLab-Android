package com.softserveacademy.feature.booking.hotel.data.di

import com.softserveacademy.feature.booking.hotel.data.repository.HotelBookingDraftRepositoryImpl
import com.softserveacademy.feature.booking.hotel.domain.repository.HotelBookingDraftRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module that handles dependency injection with Dagger Hilt.
 *
 * - @Binds: Binds DataStoreBookingRepository to the HotelBookingDraftRepository interface.
 * - @Singleton: Ensures only one instance of DataStoreBookingRepository is created in the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class HotelBookingDraftDataModule {

    @Binds
    @Singleton
    abstract fun bindHotelBookingDraftRepository(
        hotelBookingDraftRepository: HotelBookingDraftRepositoryImpl
    ): HotelBookingDraftRepository
}