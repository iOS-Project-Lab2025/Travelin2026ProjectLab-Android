package com.softserveacademy.core.data.repository

import com.softserveacademy.core.domain.repository.HotelRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module that handles dependency injection with Dagger Hilt.
 *
 * - @Binds: Binds HotelRepoImpl to the HotelRepo interface.
 * - @Singleton: Ensures only one instance of HotelRepoImpl is created in the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindHotelRepo(
        hotelRepoImpl: HotelRepoImpl
    ): HotelRepo
}