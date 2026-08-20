package com.softserveacademy.feature.favorites.common.data.di

import com.softserveacademy.feature.favorites.common.data.repository.DataStoreFavoritesRepository
import com.softserveacademy.feature.favorites.common.domain.repository.FavoritesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module that handles dependency injection for Favorites Data layer with Dagger Hilt.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class FavoritesDataModule {

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        dataStoreFavoritesRepository: DataStoreFavoritesRepository
    ): FavoritesRepository
}