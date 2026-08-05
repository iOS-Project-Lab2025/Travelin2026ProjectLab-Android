package com.softserveacademy.feature.booking.flight.presentation.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import com.softserveacademy.feature.booking.flight.presentation.R
import com.softserveacademy.feature.booking.flight.presentation.ui.screens.FlightResultsContent
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.time.Duration.Companion.hours

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], packageName = "com.softserveacademy.feature.booking.flight.presentation")
class FlightResultsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Verifies that the empty state message is shown when no flights are available.
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
                    onNext = {},
                    onBack = {},
                    onLoadMore = {},
                    onFlightSelected = {}
                )
            }
        }

        composeTestRule.onNodeWithText(emptyMessagePart, substring = true).assertIsDisplayed()
    }

    /**
     * Verifies that the network error message and Retry button are displayed
     * when the search fails due to connectivity.
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
                    onNext = {},
                    onBack = {},
                    onLoadMore = {},
                    onFlightSelected = {}
                )
            }
        }

        composeTestRule.onNodeWithText(networkErrorPart, substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText(retryButtonText).assertIsDisplayed()
    }
}

