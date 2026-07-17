package com.softserveacademy.feature.onboarding.data.repository

class OnboardingRepositoryImpl(private val corePrefs: CorePreferencesRepository) : OnboardingRepository {
    override suspend fun completeOnboarding() = corePrefs.setFirstTimeUser(false)
}