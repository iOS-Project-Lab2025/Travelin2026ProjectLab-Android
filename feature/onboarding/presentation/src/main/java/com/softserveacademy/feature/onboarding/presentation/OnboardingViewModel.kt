package com.softserveacademy.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and logic of the Onboarding screen.
 *
 * @property completeOnboardingUseCase The use case used to finalize the onboarding flow.
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase
) : ViewModel() {
    /**
     * Handles the "Get Started" button click.
     * It marks the onboarding as finished and then triggers the navigation callback.
     *
     * @param onFinished A callback function to navigate the user to the next screen (e.g., Auth/Login).
     */
    fun onGetStartedClick(onFinished: () -> Unit) {
        viewModelScope.launch {
            completeOnboardingUseCase()
            onFinished()
        }
    }
}