package com.softserveacademy.feature.onboarding.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.softserveacademy.feature.onboarding.presentation.events.OnboardingEvent
import com.softserveacademy.feature.onboarding.presentation.states.OnboardingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Onboarding screen following the MVI pattern.
 *
 * It manages the [OnboardingState] and processes [OnboardingEvent] intents.
 *
 * @property completeOnboardingUseCase Use case used to finalize the onboarding process.
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingState())
    /**
     * Observable state representing the Onboarding UI.
     */
    val uiState: StateFlow<OnboardingState> = _uiState.asStateFlow()

    /**
     * Entry point for all user interactions (intents).
     *
     * @param event The intent sent from the UI.
     */
    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.OnGetStartedClick -> loadCompleteOnboarding()
        }
    }

    private fun loadCompleteOnboarding() {
        viewModelScope.launch {
            completeOnboardingUseCase()
            _uiState.update { it.copy(isCompleted = true) }
        }
    }
}