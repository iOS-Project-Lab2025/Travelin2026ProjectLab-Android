package com.softserveacademycore.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.repository.CorePreferencesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


/**
 * ViewModel for the Splash screen.
 * Decides whether to navigate to Onboarding or Home based on user history.
 */

class SplashViewModel(
    private val preferencesRepository: CorePreferencesRepository
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination = _destination.asStateFlow()

    init {
        viewModelScope.launch {
            delay(5000.milliseconds) // Visual delay for logo
            val isFirstTime = preferencesRepository.isFirstTimeUser().first()

            _destination.value = if (isFirstTime) {
                SplashDestination.Onboarding
            } else {
                SplashDestination.Home
            }
        }
    }
}

sealed class SplashDestination {
    object Onboarding : SplashDestination()
    object Home : SplashDestination()
}