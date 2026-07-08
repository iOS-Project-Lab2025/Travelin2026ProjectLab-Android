package com.softserveacademy.feature.profile.presentation.di

import com.softserveacademy.feature.profile.domain.repository.ProfileRepository
import com.softserveacademy.feature.profile.domain.usecases.GetProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ProfileModule {

    @Provides
    @ViewModelScoped
    fun provideGetProfileUseCase(
        profileRepository: ProfileRepository
    ): GetProfileUseCase {
        return GetProfileUseCase(profileRepository)
    }
}
