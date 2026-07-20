package com.softserveacademy.feature.onboarding.data.di

import com.softserveacademy.feature.onboarding.data.repository.OnboardingRepositoryImpl
import com.softserveacademy.feature.onboarding.domain.repository.OnboardingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OnboardingDataModule {

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(
        impl: OnboardingRepositoryImpl
    ): OnboardingRepository
}