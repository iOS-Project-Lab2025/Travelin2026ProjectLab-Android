package com.softserveacademy.core.domain.model.splash

/**
 * Navigation destinations for the Splash flow, defined at Domain level.
 */
sealed class SplashDestination {
    object Onboarding : SplashDestination()
    object Login : SplashDestination()
}