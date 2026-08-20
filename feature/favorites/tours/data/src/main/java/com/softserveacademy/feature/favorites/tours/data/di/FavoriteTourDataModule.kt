package com.softserveacademy.feature.favorites.tours.data.di

import com.softserveacademy.feature.favorites.tours.data.repository.FavoriteTourRepositoryImpl
import com.softserveacademy.feature.favorites.tours.domain.repository.FavoriteTourRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteTourDataModule {

    @Binds
    @Singleton
    abstract fun bindFavoriteTourRepository(
        favoriteTourRepositoryImpl: FavoriteTourRepositoryImpl
    ): FavoriteTourRepository
}
