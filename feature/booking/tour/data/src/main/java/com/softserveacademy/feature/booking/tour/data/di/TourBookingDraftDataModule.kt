package com.softserveacademy.feature.booking.tour.data.di

import com.softserveacademy.feature.booking.tour.data.repository.TourBookingDraftRepositoryImpl
import com.softserveacademy.feature.booking.tour.domain.repository.TourBookingDraftRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TourBookingDraftDataModule {

    @Binds
    @Singleton
    abstract fun bindTourBookingDraftRepository(
        impl: TourBookingDraftRepositoryImpl
    ): TourBookingDraftRepository
}
