package com.softserveacademy.feature.booking.flight.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.softserveacademy.feature.booking.flight.domain.usecase.ValidateFlightSearchUseCase
import com.softserveacademy.feature.booking.flight.presentation.states.FlightSearchState
import com.softserveacademy.feature.booking.flight.presentation.ui.screens.FlightCriteriaContent
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UI/Compose tests for the Flight Search criteria screen.
 * Uses Robolectric to simulate the Android environment for Compose Rule.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], packageName = "com.softserveacademy.feature.booking.flight.presentation")
class FlightSearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Verifies that the global error banner appears when validation fails.
     */
    @Test
    fun whenStateHasGlobalError_bannerDisplaysValidationMessage() {
        val errorMessagePart = "Please select departure"
        val stateWithError = FlightSearchState(
            errorMessage = com.softserveacademy.feature.booking.flight.presentation.R.string.flight_error_missing_return_date
        )

        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                FlightCriteriaContent(state = stateWithError, onEvent = {}, onBack = {})
            }
        }

        composeTestRule.onNodeWithText(errorMessagePart, substring = true, ignoreCase = true).assertExists()
    }

    /**
     * Verifies that field-specific errors are displayed correctly.
     */
    @Test
    fun whenOriginHasIataError_fieldDisplaysSpecificValidationMessage() {
        val iataErrorPart = "minimum 3-letter IATA code"
        val stateWithError = FlightSearchState(
            errors = mapOf(0 to ValidateFlightSearchUseCase.SegmentError(
                originError = ValidateFlightSearchUseCase.FlightError.INVALID_ORIGIN
            ))
        )

        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                FlightCriteriaContent(state = stateWithError, onEvent = {}, onBack = {})
            }
        }

        composeTestRule.onNodeWithText(iataErrorPart, substring = true, ignoreCase = true).assertIsDisplayed()
    }
}
