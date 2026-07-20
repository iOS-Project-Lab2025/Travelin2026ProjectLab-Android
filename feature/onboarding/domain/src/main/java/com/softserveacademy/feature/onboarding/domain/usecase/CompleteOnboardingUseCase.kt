package com.softserveacademy.feature.onboarding.domain.usecase

import com.softserveacademy.feature.onboarding.domain.repository.OnboardingRepository
import javax.inject.Inject

/**
 * Use case to finalize the onboarding process.
 *
 * This component is responsible for orchestrating the call to the repository
 * to save the onboarding completion state.
 *
 * @property repository The repository where the onboarding state is persisted.
 */
class CompleteOnboardingUseCase @Inject constructor(
    private val repository: OnboardingRepository
) {
    /**
     * Executes the operation to mark onboarding as completed.
     */
    suspend operator fun invoke() = repository.completeOnboarding()
}
