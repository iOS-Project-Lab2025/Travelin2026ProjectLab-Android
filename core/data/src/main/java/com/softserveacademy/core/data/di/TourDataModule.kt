package com.softserveacademy.core.data.di

import com.softserveacademy.core.data.repository.TourBookingRepositoryImpl
import com.softserveacademy.core.data.repository.TourRepoImpl
import com.softserveacademy.core.domain.repository.TourBookingRepository
import com.softserveacademy.core.domain.repository.TourRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module that handles dependency injection for the Tour repository.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class TourDataModule {

    @Binds
    @Singleton
    abstract fun bindTourRepo(
        tourRepoImpl: TourRepoImpl
    ): TourRepo

    @Binds
    @Singleton
    abstract fun bindTourBookingRepository(
        tourBookingRepositoryImpl: TourBookingRepositoryImpl
    ): TourBookingRepository
}