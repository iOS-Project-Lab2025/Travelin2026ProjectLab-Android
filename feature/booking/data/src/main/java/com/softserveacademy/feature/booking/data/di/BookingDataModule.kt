package com.softserveacademy.feature.booking.data.di

import com.softserveacademy.feature.booking.data.repository.DataStoreBookingRepository
import com.softserveacademy.feature.booking.domain.repository.BookingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module that handles dependency injection with Dagger Hilt.
 *
 * - @Binds: Binds DataStoreBookingRepository to the BookingRepository interface.
 * - @Singleton: Ensures only one instance of DataStoreBookingRepository is created in the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class BookingDataModule {

    @Binds
    @Singleton
    abstract fun bindBookingRepository(
        dataStoreBookingRepository: DataStoreBookingRepository
    ): BookingRepository
}
