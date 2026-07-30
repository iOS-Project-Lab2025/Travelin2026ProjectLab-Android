package com.softserveacademy.feature.onboarding.presentation.events

/**
 * Represents user intentions on the Onboarding screen.
 */
sealed interface OnboardingEvent {
    /**
     * Intent triggered when the user clicks the "Get Started" button.
     */
    object OnGetStartedClick : OnboardingEvent
}