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
 *
 * Its main responsibility is to hold the splash screen active for a minimum duration
 * and then check the user's preferences to decide the next [SplashDestination].
 *
 * @property preferencesRepository Repository used to check if the user is a first-time user.
 */

class SplashViewModel(
    private val preferencesRepository: CorePreferencesRepository
) : ViewModel() {

    /**
     * Flow representing the navigation destination after the splash completes.
     * Null while the decision logic is still running.
     */
    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination = _destination.asStateFlow()

    init {
        viewModelScope.launch {
            delay(3000.milliseconds) // Visual delay for logo
            val isFirstTime = preferencesRepository.isFirstTimeUser().first()

            _destination.value = if (isFirstTime) {
                SplashDestination.Onboarding
            } else {
                SplashDestination.Login
            }
        }
    }
}

sealed class SplashDestination {
    object Onboarding : SplashDestination()
    object Login : SplashDestination()
}