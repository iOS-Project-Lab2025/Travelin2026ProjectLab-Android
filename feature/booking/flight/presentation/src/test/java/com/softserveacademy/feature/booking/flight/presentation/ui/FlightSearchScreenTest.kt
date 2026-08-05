package com.softserveacademy.feature.booking.flight.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.softserveacademy.core.domain.model.FlightType
import com.softserveacademy.feature.booking.flight.domain.usecase.ValidateFlightSearchUseCase
import com.softserveacademy.feature.booking.flight.presentation.states.FlightSearchState
import com.softserveacademy.feature.booking.flight.presentation.ui.screens.FlightCriteriaContent
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], packageName = "com.softserveacademy.feature.booking.flight.presentation")
class FlightSearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Verifies that the error banner displays the correct message
     * when a critical validation error (missing return date) is present.
     */
    @Test
    fun whenStateHasGlobalError_bannerDisplaysMissingReturnDateMessage() {
        val errorMessagePart = "Please select departure"
        val stateWithError = FlightSearchState(
            errorMessage = com.softserveacademy.feature.booking.flight.presentation.R.string.flight_error_missing_return_date
        )

        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                FlightCriteriaContent(state = stateWithError, onEvent = {}, onBack = {})
            }
        }

        // Using substring = true to handle potential formatting/newline issues
        composeTestRule.onNodeWithText(errorMessagePart, substring = true).assertIsDisplayed()
    }

    /**
     * Confirms that the specific IATA validation error is visible under the origin input.
     */
    @Test
    fun whenOriginHasIataError_fieldDisplaysValidationMessage() {
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

        composeTestRule.onNodeWithText(iataErrorPart, substring = true).assertIsDisplayed()
    }

    /**
     * Checks if segment labels (e.g., "Flight 1") and the addition button
     * are visible in Multi-city mode.
     */
    @Test
    fun whenMultiCityModeIsActive_segmentLabelsAndAddButtonAreVisible() {
        val multiCityState = FlightSearchState(
            selectedFlightType = FlightType.MULTI_CITY,
            segments = listOf(
                com.softserveacademy.core.domain.model.FlightSegment(),
                com.softserveacademy.core.domain.model.FlightSegment()
            )
        )

        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                FlightCriteriaContent(state = multiCityState, onEvent = {}, onBack = {})
            }
        }

        composeTestRule.onNodeWithText("Flight 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Add Flight").assertExists()
        composeTestRule.onNodeWithText("Flight 2").assertExists()
    }
}