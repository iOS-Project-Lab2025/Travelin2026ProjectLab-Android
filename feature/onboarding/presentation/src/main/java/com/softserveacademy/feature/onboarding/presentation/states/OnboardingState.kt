package com.softserveacademy.feature.onboarding.presentation.states

/**
 * Represents the UI state for the Onboarding screen.
 *
 * @property isCompleted Indicates if the onboarding process has been finished successfully.
 */
data class OnboardingState(
    val isCompleted: Boolean = false
)