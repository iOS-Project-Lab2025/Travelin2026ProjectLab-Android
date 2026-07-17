package com.softserveacademy.feature.onboarding.domain.usecase

class CompleteOnboardingUseCase(private val repository: OnboardingRepository) {
    suspend operator fun invoke() = repository.completeOnboarding()
}