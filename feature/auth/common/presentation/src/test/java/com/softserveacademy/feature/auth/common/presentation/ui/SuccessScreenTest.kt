package com.softserveacademy.feature.auth.common.presentation.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UI tests for [SuccessScreen] using Robolectric.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class SuccessScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `given success screen when rendered then success message is visible`() {
        // GIVEN
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                SuccessScreen(onExploreClick = {})
            }
        }

        // THEN
        composeTestRule.onNodeWithText("Successfully created an account").assertExists()
    }
}
