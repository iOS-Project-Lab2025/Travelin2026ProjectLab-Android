package com.softserveacademy.feature.onboarding.domain.repository

/**
 * Repository interface for managing onboarding-related data operations.
 * This interface abstracts the persistence logic for the onboarding flow.
 */
interface OnboardingRepository {

    /**
     * Marks the onboarding process as completed in the data source.
     * This ensures the user doesn't see the onboarding screen on subsequent launches.
     */
    suspend fun completeOnboarding()
}