package com.softserveacademy.home.presentation.di

import com.softserveacademy.feature.auth.common.domain.usecase.LogoutUseCase
import com.softserveacademy.feature.auth.common.domain.SessionRepository
import com.softserveacademy.home.domain.repository.ProfileRepository
import com.softserveacademy.home.domain.usecases.GetProfileUseCase
import com.softserveacademy.home.domain.usecases.UpdateProfileUseCase
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

    /**
     * Provides the [UpdateProfileUseCase] dependency.
     */
    @Provides
    @ViewModelScoped
    fun provideUpdateProfileUseCase(
        profileRepository: ProfileRepository
    ): UpdateProfileUseCase {
        return UpdateProfileUseCase(profileRepository)
    }

    /**
     * Provides the [LogoutUseCase] dependency.
     */
    @Provides
    @ViewModelScoped
    fun provideLogoutUseCase(
        sessionRepository: SessionRepository
    ): LogoutUseCase {
        return LogoutUseCase(sessionRepository)
    }
}
