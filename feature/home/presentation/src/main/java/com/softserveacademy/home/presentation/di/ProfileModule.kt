package com.softserveacademy.home.presentation.di

import com.softserveacademy.home.domain.repository.ProfileRepository
import com.softserveacademy.home.domain.usecases.GetProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Hilt module for providing profile-related presentation dependencies.
 */
@Module
@InstallIn(ViewModelComponent::class)
object ProfileModule {

    /**
     * Provides the [GetProfileUseCase] dependency.
     */
    @Provides
    @ViewModelScoped
    fun provideGetProfileUseCase(
        profileRepository: ProfileRepository
    ): GetProfileUseCase {
        return GetProfileUseCase(profileRepository)
    }
}
