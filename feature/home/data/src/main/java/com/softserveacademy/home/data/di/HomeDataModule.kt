package com.softserveacademy.home.data.di

import com.softserveacademy.home.data.repository.HomeRepositoryImpl
import com.softserveacademy.home.domain.repository.HomeRepository
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
abstract class HomeDataModule {

    @Binds
    @Singleton
    abstract fun bindHotelRepo(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository
}