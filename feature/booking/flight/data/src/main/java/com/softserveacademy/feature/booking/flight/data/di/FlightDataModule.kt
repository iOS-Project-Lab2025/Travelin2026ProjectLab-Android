package com.softserveacademy.feature.booking.flight.data.di

import com.softserveacademy.feature.booking.flight.data.remote.FlightRemoteDataSource
import com.softserveacademy.feature.booking.flight.data.remote.MockFlightRemoteDataSource
import com.softserveacademy.feature.booking.flight.data.repository.FlightBookingDraftRepositoryImpl
import com.softserveacademy.feature.booking.flight.data.repository.FlightBookingRepositoryImpl
import com.softserveacademy.feature.booking.flight.data.repository.FlightRepositoryImpl
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingDraftRepository
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingRepository
import com.softserveacademy.feature.booking.flight.domain.repository.FlightRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module to provide dependencies for the flight data layer.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class FlightDataModule {

    @Binds
    @Singleton
    abstract fun bindFlightRemoteDataSource(
        mockDataSource: MockFlightRemoteDataSource
    ): FlightRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindFlightRepository(
        repositoryImpl: FlightRepositoryImpl
    ): FlightRepository

    @Binds
    @Singleton
    abstract fun bindFlightBookingDraftRepository(
        impl: FlightBookingDraftRepositoryImpl
    ): FlightBookingDraftRepository

    /**
     * Binds the FlightBookingRepository interface to its implementation.
     */
    @Binds
    @Singleton
    abstract fun bindFlightBookingRepository(
        impl: FlightBookingRepositoryImpl
    ): FlightBookingRepository
}