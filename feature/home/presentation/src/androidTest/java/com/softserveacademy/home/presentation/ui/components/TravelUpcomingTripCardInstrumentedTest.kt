package com.softserveacademy.home.presentation.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.home.presentation.model.UpcomingTripUi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TravelUpcomingTripCardInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleTrip = UpcomingTripUi(
        status = "Upcoming",
        date = "Sat, Nov 23",
        originCode = "CGK",
        originTime = "09:00",
        destinationCode = "DPS",
        destinationTime = "10:30",
        duration = "1h 30m",
        airline = "Garuda Indonesia",
        travelClass = "Economy",
        flightType = "Direct",
        bookingId = "GA-880"
    )

    @Test
    fun displaysBookingId() {
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                TravelUpcomingTripCard(trip = sampleTrip)
            }
        }
        composeTestRule.onNodeWithText("GA-880").assertIsDisplayed()
    }

    @Test
    fun displaysAirportCodes() {
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                TravelUpcomingTripCard(trip = sampleTrip)
            }
        }
        composeTestRule.onNodeWithText("CGK").assertIsDisplayed()
        composeTestRule.onNodeWithText("DPS").assertIsDisplayed()
    }

    @Test
    fun displaysFlightInfo() {
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                TravelUpcomingTripCard(trip = sampleTrip)
            }
        }
        composeTestRule.onNode(hasText("Garuda Indonesia", substring = true)).assertIsDisplayed()
    }

    @Test
    fun displaysStatus() {
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                TravelUpcomingTripCard(trip = sampleTrip)
            }
        }
        composeTestRule.onNodeWithText("Upcoming").assertIsDisplayed()
    }
}
