package com.softserveacademy.feature.auth.login.presentation.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UI tests for [ForgotPasswordScreen] using Robolectric.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class ForgotPasswordScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `given forgot password screen when rendered then recover button is visible`() {
        // GIVEN
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                ForgotPasswordContent(
                    email = "",
                    onEmailChange = {},
                    isLoading = false,
                    error = null,
                    isSuccess = false,
                    onRecoverClick = {},
                    onNavigateBack = {},
                    navonRecoverClick = {}
                )
            }
        }

        // THEN
        composeTestRule.onNodeWithText("Recover Password").assertExists()
    }
}
