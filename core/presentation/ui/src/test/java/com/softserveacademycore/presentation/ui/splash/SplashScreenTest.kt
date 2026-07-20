package com.softserveacademycore.presentation.ui.splash

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.softserveacademy.core.domain.repository.CorePreferencesRepository
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UI Test for SplashScreen.
 * Verifies the presence of the brand logo on the screen.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val repository = mockk<CorePreferencesRepository>()

    @Test
    fun `given splash screen when displayed then brand logo is visible`() {
        // GIVEN
        every { repository.isFirstTimeUser() } returns flowOf(true)
        val viewModel = SplashViewModel(repository)

        // WHEN
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                SplashScreen(
                    viewModel = viewModel,
                    onNavigateToOnboarding = {},
                    onNavigateToLogin = {}
                )
            }
        }

        // THEN: Validates that the logo with the description is present
        composeTestRule.onNodeWithContentDescription("Travelin Official Logo").assertIsDisplayed()
    }
}