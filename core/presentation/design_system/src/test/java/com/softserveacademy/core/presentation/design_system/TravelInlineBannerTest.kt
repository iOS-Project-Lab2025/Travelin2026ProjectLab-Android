package com.softserveacademy.core.presentation.design_system

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.softserveacademy.core.presentation.design_system.components.InlineErrorBanner
import com.softserveacademy.core.presentation.design_system.components.InlineSuccessBanner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UI Tests for Inline Banners using Robolectric.
 * Uses a minimal MaterialTheme in tests to avoid resolving app-specific resources at runtime.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], manifest = Config.NONE)
class TravelInlineBannerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `given isVisible is true when ErrorBanner renders then message is displayed`() {
        val testMessage = "Invalid email format"

        // GIVEN: The banner is visible (using minimal MaterialTheme to avoid theme resource fetches)
        composeTestRule.setContent {
            MaterialTheme {
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

        // GIVEN: Banner is hidden
        composeTestRule.setContent {
            MaterialTheme {
                InlineErrorBanner(
                    message = testMessage,
                    isVisible = false
                )
            }
        }

        // THEN: The text should not exist on the screen
        composeTestRule.onNodeWithText(testMessage).assertDoesNotExist()
    }

    @Test
    fun `given success banner when rendered then displays correct success message`() {
        val successMsg = "Registration Complete!"

        composeTestRule.setContent {
            MaterialTheme {
                InlineSuccessBanner(
                    message = successMsg,
                    isVisible = true
                )
            }
        }

        composeTestRule.onNodeWithText(successMsg).assertIsDisplayed()
    }
}
