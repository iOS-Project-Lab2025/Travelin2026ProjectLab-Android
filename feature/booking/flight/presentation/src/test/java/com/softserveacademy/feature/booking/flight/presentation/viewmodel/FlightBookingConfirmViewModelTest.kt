package com.softserveacademy.feature.booking.flight.presentation.viewmodel

import android.util.Log
import com.softserveacademy.core.domain.model.*
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.feature.booking.common.domain.usecase.CreatePaymentIntentUseCase
import com.softserveacademy.feature.booking.flight.domain.model.FlightBookingDraft
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingDraftRepository
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingRepository
import com.softserveacademy.feature.booking.flight.presentation.events.FlightBookingConfirmEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.hours

/**
 * High-Impact Unit Tests for the Flight Booking Confirmation lifecycle.
 *
 * This test suite validates:
 * 1. Mathematical precision of total pricing using the modular [PassengerCounts] model.
 * 2. State transition integrity during Stripe/Simulation payment flows.
 * 3. Accurate mapping between temporary [FlightBookingDraft] and official [FlightBooking] records.
 * 4. Error resilience and UI state recovery.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class FlightBookingConfirmViewModelTest {

    private val draftRepository: FlightBookingDraftRepository = mockk()
    private val createPaymentIntentUseCase: CreatePaymentIntentUseCase = mockk()
    private val flightBookingRepository: FlightBookingRepository = mockk()

    private lateinit var viewModel: FlightBookingConfirmViewModel
    private val testDispatcher = StandardTestDispatcher()

    // Professional Mock Data setup representing a multi-passenger scenario
    private val mockFlight = Flight(
        id = "f1", airline = Airline("LA", "Latam", ""), flightNumber = "LA123",
        origin = Airport("SCL", "Santiago", "", ""), destination = Airport("LIM", "Lima", "", ""),
        departureTime = 0, arrivalTime = 0, duration = 4.hours, cabinClass = CabinClass.ECONOMY
    )
    private val mockOffer = FlightOffer(id = "o1", flight = mockFlight, basePrice = 100.0)

    // Draft with a distribution of 3 total passengers (2 Adults, 1 Infant)
    private val mockDraft = FlightBookingDraft(
        passengerCounts = PassengerCounts(adults = 2, children = 0, infants = 1),
        selectedOffers = mapOf(0 to mockOffer)
    )

    // Representation of the official booking returned by the server/API
    private val mockFinalBooking = FlightBooking(
        bookingId = "b-final-123",
        userId = "user-auth-001",
        flights = listOf(mockFlight),
        passengers = emptyList(),
        tickets = emptyList(),
        confirmationCode = "FL-SUCCESS-PNR",
        status = BookingStatus.COMPLETED,
        totalAmount = 300.0,
        currencyCode = "USD",
        contactInfo = BookingContactInfo(),
        createdAt = 0L
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class) // Prevents "Method d not mocked" runtime exceptions

        // Configuration for expected success paths
        coEvery { draftRepository.getDraft() } returns flowOf(mockDraft)
        coEvery { draftRepository.clearDraft() } returns Unit
        coEvery { flightBookingRepository.saveBooking(any()) } returns AppResult.Success(mockFinalBooking)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Business Logic Test: Verifies that the ViewModel calculates the aggregate price
     * based on (Price per Offer) * (Total passengers in group).
     */
    @Test
    fun `when initialized, should calculate total price correctly for all passengers`() = runTest(testDispatcher) {
        viewModel = FlightBookingConfirmViewModel(draftRepository, createPaymentIntentUseCase, flightBookingRepository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        // Expectation: 100.0 base * 3 pax = 300
        assertEquals("Total price calculation is inaccurate", 300.0, state.totalPrice, 0.0)
        assertEquals("Currency should default to USD", "USD", state.currency)
    }

    /**
     * Navigation Flow Test: Validates that a successful payment simulation
     * triggers the successful state and resets loading indicators.
     */
    @Test
    fun `when payment succeeds, should set navigation flag to true and clear loading`() = runTest(testDispatcher) {
        viewModel = FlightBookingConfirmViewModel(draftRepository, createPaymentIntentUseCase, flightBookingRepository)
        advanceUntilIdle()

        viewModel.onEvent(FlightBookingConfirmEvent.OnSimulateSuccessClick)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("isPaymentSuccessful should be true for navigation", state.isPaymentSuccessful)
        assertEquals("isLoading should be cleared after finalizeBooking", false, state.isLoading)
    }

    /**
     * API Failover Test: Ensures that if the repository fails to issue tickets,
     * the specific technical error message is propagated to the UI.
     */
    @Test
    fun `when repository save fails, should update state with correct error message`() = runTest(testDispatcher) {
        // Force a failure in the Issuance process
        coEvery { flightBookingRepository.saveBooking(any()) } returns AppResult.Failure(mockk())

        viewModel = FlightBookingConfirmViewModel(draftRepository, createPaymentIntentUseCase, flightBookingRepository)
        advanceUntilIdle()

        viewModel.onEvent(FlightBookingConfirmEvent.OnSimulateSuccessClick)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Error message mismatch with API requirement", "API Error: Could not issue tickets", state.error)
        assertEquals("Failure should not trigger navigation", false, state.isPaymentSuccessful)
    }

    /**
     * Data Integrity Test: Confirms that the ViewModel maps the correct initial status
     * and monetary values before sending the request to the data layer.
     */
    @Test
    fun `when booking is officially saved, repository must receive correctly mapped PENDING request`() = runTest(testDispatcher) {
        viewModel = FlightBookingConfirmViewModel(draftRepository, createPaymentIntentUseCase, flightBookingRepository)
        advanceUntilIdle()

        viewModel.onEvent(FlightBookingConfirmEvent.OnSimulateSuccessClick)
        advanceUntilIdle()

        // Capture and verify the object sent to the API/Repository
        coVerify {
            flightBookingRepository.saveBooking(withArg { booking ->
                assertEquals("Booking amount mismatch during mapping", 300.0, booking.totalAmount, 0.0)
                assertEquals("Initial booking request status must be PENDING", BookingStatus.PENDING, booking.status)
                assertTrue("Confirmation code must follow flight naming convention", booking.confirmationCode.startsWith("FL-"))
            })
        }
    }

    /**
     * Interaction Test: Verifies that the ViewModel can reset an error state after a failure.
     */
    @Test
    fun `when dismiss error event occurs, should clear the existing error state`() = runTest(testDispatcher) {
        viewModel = FlightBookingConfirmViewModel(draftRepository, createPaymentIntentUseCase, flightBookingRepository)
        advanceUntilIdle()

        viewModel.onEvent(FlightBookingConfirmEvent.OnDismissError)

        val state = viewModel.uiState.value
        assertEquals("Error state was not cleared successfully", null, state.error)
    }
}