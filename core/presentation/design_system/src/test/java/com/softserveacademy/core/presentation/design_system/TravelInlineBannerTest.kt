package com.softserveacademy.core.presentation.design_system

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.softserveacademy.core.presentation.design_system.components.InlineErrorBanner
import com.softserveacademy.core.presentation.design_system.components.InlineSuccessBanner
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UI Tests for Inline Banners using Robolectric.
 * Verifies visibility states and message rendering.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33]) // Simulates Android 13
class TravelInlineBannerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `given isVisible is true when ErrorBanner renders then message is displayed`() {
        val testMessage = "Invalid email format"

        // GIVEN: The banner is visible
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                InlineErrorBanner(
                    message = testMessage,
                    isVisible = true
                )
            }
        }

        // THEN: The message must be found on the screen
        composeTestRule.onNodeWithText(testMessage).assertIsDisplayed()
    }

    @Test
    fun `given isVisible is false when ErrorBanner renders then message does not exist`() {
        val testMessage = "Secret error"

        // GIVEN: El banner está oculto
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                InlineErrorBanner(
                    message = testMessage,
                    isVisible = false
                )
            }
        }

        // THEN: El texto no debe existir en la pantalla
        composeTestRule.onNodeWithText(testMessage).assertDoesNotExist()
    }

    @Test
    fun `given success banner when rendered then displays correct success message`() {
        val successMsg = "Registration Complete!"

        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                InlineSuccessBanner(
                    message = successMsg,
                    isVisible = true
                )
            }
        }

        composeTestRule.onNodeWithText(successMsg).assertIsDisplayed()
    }
}