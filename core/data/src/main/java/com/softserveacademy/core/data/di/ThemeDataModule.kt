package com.softserveacademy.core.data.di

import com.softserveacademy.core.data.repository.ThemeRepositoryImpl
import com.softserveacademy.core.domain.repository.ThemeRepository
import com.softserveacademy.core.domain.usecase.GetThemeUseCase
import com.softserveacademy.core.domain.usecase.SetThemeUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ThemeDataModule {

    @Binds
    @Singleton
    abstract fun bindThemeRepository(
        themeRepositoryImpl: ThemeRepositoryImpl
    ): ThemeRepository

    companion object {
        @Provides
        @Singleton
        fun provideGetThemeUseCase(
            themeRepository: ThemeRepository
        ): GetThemeUseCase {
            return GetThemeUseCase(themeRepository)
        }

        @Provides
        @Singleton
        fun provideSetThemeUseCase(
            themeRepository: ThemeRepository
        ): SetThemeUseCase {
            return SetThemeUseCase(themeRepository)
        }
    }
}
