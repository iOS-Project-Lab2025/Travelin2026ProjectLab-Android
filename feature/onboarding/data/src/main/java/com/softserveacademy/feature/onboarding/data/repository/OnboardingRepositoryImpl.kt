package com.softserveacademy.feature.onboarding.data.repository

import com.softserveacademy.core.domain.repository.CorePreferencesRepository
import com.softserveacademy.feature.onboarding.domain.repository.OnboardingRepository
import javax.inject.Inject

/**
 * Implementation of [OnboardingRepository] that uses core preferences for persistence.
 *
 * @property corePrefs The core preferences repository used to update the "first time user" flag.
 */
class OnboardingRepositoryImpl @Inject constructor(
    private val corePrefs: CorePreferencesRepository
) : OnboardingRepository {
    /**
     * Updates the shared preferences to indicate that the user is no longer a first-time user.
     */
    override suspend fun completeOnboarding() {
        corePrefs.setFirstTimeUser(false)
    }
}