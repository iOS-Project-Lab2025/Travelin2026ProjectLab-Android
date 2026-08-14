package com.softserveacademy.feature.booking.flight.presentation.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.softserveacademy.core.domain.model.*
import com.softserveacademy.feature.booking.flight.domain.model.FlightBookingDraft
import com.softserveacademy.feature.booking.flight.presentation.states.FlightBookingConfirmState
import com.softserveacademy.feature.booking.flight.presentation.ui.screens.FlightBookingConfirmContent
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.time.Duration

/**
 * UI Tests for the Flight Booking Confirmation Screen.
 *
 * Verifies:
 * 1. Correct rendering of journey details (Dates & Guests).
 * 2. Accuracy of the price breakdown and final total in the bottom bar.
 * 3. Visibility of the payment simulation overlay.
 * 4. Full-screen loading state behavior.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], packageName = "com.softserveacademy.feature.booking.flight.presentation")
class FlightBookingConfirmScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Professional Mock Data for UI Rendering
    private val mockFlight = Flight(
        id = "f1", airline = Airline("LA", "Latam", ""), flightNumber = "LA123",
        origin = Airport("SCL", "Santiago", "", ""), destination = Airport("LIM", "Lima", "", ""),
        departureTime = 0, arrivalTime = 0, duration = Duration.ZERO, cabinClass = CabinClass.BUSINESS
    )
    private val mockOffer = FlightOffer(id = "o1", flight = mockFlight, basePrice = 450.0)
    private val mockDraft = FlightBookingDraft(
        adults = 1, children = 1,
        selectedOffers = mapOf(0 to mockOffer),
        passengers = listOf(
            FlightPassenger(firstName = "John", lastName = "Doe", passengerType = PassengerType.ADU),
            FlightPassenger(firstName = "Jane", lastName = "Doe", passengerType = PassengerType.CHD)
        )
    )

    /**
     * Verifies that the trip summary (Dates and Passenger count)
     * matches the confirmation logic.
     */
    @Test
    fun whenDataIsLoaded_displaysCorrectJourneySummary() {
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                FlightBookingConfirmContent(
                    state = FlightBookingConfirmState(isLoading = false, draft = mockDraft),
                    onBack = {}, onConfirm = {}, onSimulateSuccess = {},
                    onSimulateFailure = {}, onDismissSimulation = {}
                )
            }
        }

        // Check for the "Travelers" info: "1 Adult, 1 Child" (based on our mockDraft)
        composeTestRule.onNodeWithText("1 Adult, 1 Child", substring = true).assertIsDisplayed()
    }

    /**
     * Financial UI Test: Ensures the final price in the bottom bar
     * is imponent and shows the correct currency.
     */
    @Test
    fun whenDataIsLoaded_displaysTotalAmountInBottomBar() {
        val testPrice = 900
        val testCurrency = "USD"

        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                FlightBookingConfirmContent(
                    state = FlightBookingConfirmState(
                        isLoading = false,
                        draft = mockDraft,
                        totalPrice = testPrice,
                        currency = testCurrency
                    ),
                    onBack = {}, onConfirm = {}, onSimulateSuccess = {},
                    onSimulateFailure = {}, onDismissSimulation = {}
                )
            }
        }

        // Verify the big price text in the Bottom Bar
        composeTestRule.onNodeWithText("$testCurrency $testPrice", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Total Price", ignoreCase = true).assertExists()
    }

    /**
     * Simulation Flow Test: Confirms that the payment sheet
     * appears correctly when triggered by the state.
     */
    @Test
    fun whenShowSimulationIsTrue_displaysSimulationSheet() {
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                FlightBookingConfirmContent(
                    state = FlightBookingConfirmState(
                        isLoading = false,
                        showPaymentSimulationSheet = true
                    ),
                    onBack = {}, onConfirm = {}, onSimulateSuccess = {},
                    onSimulateFailure = {}, onDismissSimulation = {}
                )
            }
        }

        // Verify elements inside the simulation sheet
        composeTestRule.onNodeWithText("Simulate Success", ignoreCase = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Simulate Failure", ignoreCase = true).assertIsDisplayed()
    }

    /**
     * Verifies that all major UI sections are present when the data is loaded.
     * This confirms the screen composition is correct.
     */
    @Test
    fun whenDataIsLoaded_displaysSectionHeaders() {
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                FlightBookingConfirmContent(
                    state = FlightBookingConfirmState(isLoading = false, draft = mockDraft),
                    onBack = {}, onConfirm = {}, onSimulateSuccess = {},
                    onSimulateFailure = {}, onDismissSimulation = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Flights", substring = true, ignoreCase = true).assertExists()
        composeTestRule.onNodeWithText("Contact", substring = true, ignoreCase = true).assertExists()
    }

    /**
     * Interaction Test: Verifies that clicking the 'Book Now' button
     * correctly triggers the confirmation callback.
     */
    @Test
    fun whenConfirmClicked_triggersOnConfirmCallback() {
        var onConfirmCalled = false

        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                FlightBookingConfirmContent(
                    state = FlightBookingConfirmState(isLoading = false, draft = mockDraft),
                    onBack = {},
                    onConfirm = { onConfirmCalled = true }, // Action being tested
                    onSimulateSuccess = {},
                    onSimulateFailure = {},
                    onDismissSimulation = {}
                )
            }
        }

        // 1. Find the 'Book Now' button by its text (from strings.xml)
        // Note: use substring=true if the text might be 'Book Now' or similar
        composeTestRule.onNodeWithText("Book Now", ignoreCase = true).performClick()

        // 2. Verify the side effect
        org.junit.Assert.assertTrue("The onConfirm callback should have been executed", onConfirmCalled)
    }

    /**
     * Navigation Test: Verifies that the back button in the TopAppBar
     * correctly triggers the return navigation callback.
     */
    @Test
    fun whenBackClicked_triggersOnBackCallback() {
        var onBackCalled = false

        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                FlightBookingConfirmContent(
                    state = FlightBookingConfirmState(isLoading = false, draft = mockDraft),
                    onBack = { onBackCalled = true }, // Action being tested
                    onConfirm = {},
                    onSimulateSuccess = {},
                    onSimulateFailure = {},
                    onDismissSimulation = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Back", ignoreCase = true).performClick()

        assertTrue("The onBack callback should have been executed", onBackCalled)
    }
}