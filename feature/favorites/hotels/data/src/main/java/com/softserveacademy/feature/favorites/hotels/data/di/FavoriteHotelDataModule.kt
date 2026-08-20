package com.softserveacademy.feature.favorites.hotels.data.di

import com.softserveacademy.feature.favorites.hotels.data.repository.FavoriteHotelRepositoryImpl
import com.softserveacademy.feature.favorites.hotels.domain.repository.FavoriteHotelRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteHotelDataModule {

    @Binds
    @Singleton
    abstract fun bindFavoriteHotelRepository(
        favoriteHotelRepositoryImpl: FavoriteHotelRepositoryImpl
    ): FavoriteHotelRepository
}
