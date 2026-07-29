package com.softserveacademycore.presentation.ui.splash.events

/**
 * Events sent from the UI to the SplashViewModel.
 */
sealed interface SplashEvent {
    // Event to when the screen is ready to start the logic
    object OnViewReady : SplashEvent
}