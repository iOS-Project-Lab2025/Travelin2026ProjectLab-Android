package com.softserveacademy.core.domain.usecase.splash

import com.softserveacademy.core.domain.model.splash.SplashDestination
import com.softserveacademy.core.domain.repository.CorePreferencesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first

/**
 * Business logic to decide the initial screen of the app.
 */
class GetSplashDestinationUseCase @Inject constructor(
    private val repository: CorePreferencesRepository
) {
    suspend operator fun invoke(): SplashDestination {
        val isFirstTime = repository.isFirstTimeUser().first()
        return if (isFirstTime) {
            SplashDestination.Onboarding
        } else {
            SplashDestination.Login
        }
    }
}