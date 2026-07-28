package com.softserveacademy.feature.auth.login.presentation.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UI tests for [LoginScreen] using Robolectric.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `given login screen when rendered then sign in button is visible`() {
        // GIVEN
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                LoginContent(
                    email = "",
                    onEmailChange = {},
                    password = "",
                    onPasswordChange = {},
                    isLoading = false,
                    error = null,
                    onLoginClick = {},
                    onNavigateToForgotPassword = {},
                    onNavigateToRegister = {}
                )
            }
        }

        // THEN
        composeTestRule.onNodeWithText("Sign In").assertExists()
    }

    @Test
    fun `given error message when rendered then error text is visible`() {
        // GIVEN
        val errorMessage = "Invalid credentials"
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                LoginContent(
                    email = "",
                    onEmailChange = {},
                    password = "",
                    onPasswordChange = {},
                    isLoading = false,
                    error = errorMessage,
                    onLoginClick = {},
                    onNavigateToForgotPassword = {},
                    onNavigateToRegister = {}
                )
            }
        }

        // THEN
        composeTestRule.onNodeWithText(errorMessage).assertExists()
    }
}
