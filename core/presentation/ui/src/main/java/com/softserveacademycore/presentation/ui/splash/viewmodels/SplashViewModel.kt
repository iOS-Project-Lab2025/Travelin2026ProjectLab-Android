package com.softserveacademycore.presentation.ui.splash.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.splash.SplashDestination
import com.softserveacademy.core.domain.usecase.splash.GetSplashDestinationUseCase
import com.softserveacademycore.presentation.ui.splash.events.SplashEvent
import com.softserveacademycore.presentation.ui.splash.states.SplashState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


/**
 * ViewModel for the Splash screen.
 *
 * Its main responsibility is to hold the splash screen active for a minimum duration
 * and then check the user's preferences to decide the next [SplashDestination].
 *
 * @property getSplashDestinationUseCase Use case to decide the initial screen based on user state.
 */

class SplashViewModel(
    private val getSplashDestinationUseCase: GetSplashDestinationUseCase
) : ViewModel() {

    /**
     * Flow representing the navigation destination after the splash completes.
     * Null while the decision logic is still running.
     */
    private val _uiState = MutableStateFlow(SplashState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: SplashEvent) {
        when (event) {
            SplashEvent.OnViewReady -> loadSplashDestination()
        }
    }

    private fun loadSplashDestination() {
        viewModelScope.launch {
            delay(3000.milliseconds)
            val destination = getSplashDestinationUseCase()

            _uiState.update {
                it.copy(
                    destination = destination,
                    isLoading = false
                )
            }
        }
    }
}