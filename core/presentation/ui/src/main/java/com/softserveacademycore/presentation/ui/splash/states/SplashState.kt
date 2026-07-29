package com.softserveacademycore.presentation.ui.splash.states

import com.softserveacademy.core.domain.model.splash.SplashDestination

/**
 * State representing the Splash screen data.
 */
data class SplashState(
    val destination: SplashDestination? = null,
    val isLoading: Boolean = true
)