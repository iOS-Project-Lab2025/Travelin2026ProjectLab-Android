package com.softserveacademy.feature.booking.flight.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.softserveacademy.feature.booking.flight.presentation.R
import com.softserveacademy.feature.booking.flight.presentation.ui.screens.FlightResultsContent
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UI/Compose tests for the Flight Results list.
 * Verifies states such as empty list, technical errors, and segment progress.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], packageName = "com.softserveacademy.feature.booking.flight.presentation")
class FlightResultsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Requirement US1: Verify that the empty state is explicitly shown
     * when search yields zero results.
     */
    @Test
    fun whenOffersListIsEmpty_displaysEmptyStateMessage() {
        val emptyMessagePart = "No flights found"

        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                FlightResultsContent(
                    visibleOffers = emptyList(),
                    origin = "SCL",
                    destination = "LIM",
                    passengerCount = 1,
                    totalAvailable = 0,
                    isLoading = false,
                    error = null,
                    currentSegmentIndex = 0,
                    totalSegments = 1,
                    selectedOfferId = null,
                    currencyCode = "USD",
                    exchangeRate = 1.0,
                    isNextEnabled = false,
                    onNext = {},
                    onBack = {},
                    onLoadMore = {},
                    onRetry = {},
                    onFlightSelected = {}
                )
            }
        }

        composeTestRule.onNodeWithText(emptyMessagePart, substring = true).assertIsDisplayed()
    }

    /**
     * Requirement US1: Confirm that network failures display an error UI
     * including a clearly labeled Retry button.
     */
    @Test
    fun whenNetworkErrorOccurs_displaysErrorMessageAndRetryButton() {
        val networkErrorPart = "No internet connection"
        val retryButtonText = "Retry"

        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                FlightResultsContent(
                    visibleOffers = emptyList(),
                    origin = "SCL",
                    destination = "LIM",
                    passengerCount = 1,
                    totalAvailable = 0,
                    isLoading = false,
                    error = R.string.flight_error_network,
                    currentSegmentIndex = 0,
                    totalSegments = 1,
                    selectedOfferId = null,
                    currencyCode = "USD",
                    exchangeRate = 1.0,
                    isNextEnabled = false,
                    onNext = {},
                    onBack = {},
                    onLoadMore = {},
                    onRetry = {},
                    onFlightSelected = {}
                )
            }
        }

        composeTestRule.onNodeWithText(networkErrorPart, substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText(retryButtonText, ignoreCase = true).assertIsDisplayed()
    }

    /**
     * Verifies that the flight selection progress (e.g., "Flight 1 of 2")
     * is visible in Multi-step flows.
     */
    @Test
    fun whenMultipleSegmentsExist_displaysProgressIndicator() {
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                FlightResultsContent(
                    visibleOffers = emptyList(),
                    origin = "SCL",
                    destination = "LIM",
                    passengerCount = 1,
                    totalAvailable = 0,
                    isLoading = false,
                    error = null,
                    currentSegmentIndex = 1, // Second segment
                    totalSegments = 2,
                    selectedOfferId = null,
                    currencyCode = "USD",
                    exchangeRate = 1.0,
                    isNextEnabled = false,
                    onNext = {},
                    onBack = {},
                    onLoadMore = {},
                    onRetry = {},
                    onFlightSelected = {}
                )
            }
        }

        // Checks for "Flight 2 of 2" indicator
        composeTestRule.onNodeWithText("Flight 2 of 2", substring = true).assertIsDisplayed()
    }
}

