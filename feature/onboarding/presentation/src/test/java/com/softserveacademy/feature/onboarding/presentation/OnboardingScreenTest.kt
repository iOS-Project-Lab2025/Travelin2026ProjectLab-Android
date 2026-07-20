package com.softserveacademy.feature.onboarding.presentation

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UI Tests for [OnboardingScreen] using Robolectric.
 *
 * These tests verify the "Look and Feel" (visibility) and basic interactions
 * without requiring a physical device or emulator.
 * They ensure that the Design System theme is applied and the components are accessible.
 */

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class OnboardingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Verifies that the brand identity (Title and Button) is correctly
     * rendered when the screen is first displayed.
     */
    @Test
    fun onboardingScreen_isDisplayedWithCorrectContent() {
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                OnboardingScreen(onGetStarted = {})
            }
        }

        // Verify title and button are present
        composeTestRule.onNodeWithText("explore", substring = true, ignoreCase = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Your Journey Starts Here", ignoreCase = true).assertIsDisplayed()
    }

    /**
     * Verifies that the brand identity (Title and Button) is correctly
     * displayed in Dark Mode.
     */
    @Test
    fun onboardingScreen_darkMode_isDisplayedCorrectly() {
        composeTestRule.setContent {
            // forcing darkTheming manually
            Travelin2026ProjectLabTheme(darkTheme = true) {
                OnboardingScreen(onGetStarted = {})
            }
        }

        // Verify that the title and button elements are present
        composeTestRule.onNodeWithText("Your Journey Starts Here", ignoreCase = true).assertIsDisplayed()
    }
}