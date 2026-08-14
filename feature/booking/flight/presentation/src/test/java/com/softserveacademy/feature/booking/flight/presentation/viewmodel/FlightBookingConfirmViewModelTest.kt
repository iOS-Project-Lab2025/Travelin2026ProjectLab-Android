package com.softserveacademy.feature.booking.flight.presentation.viewmodel

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
 * High-Impact Unit Tests for Flight Booking Confirmation.
 *
 * Objectives:
 * 1. Ensure mathematical accuracy of trip pricing.
 * 2. Validate state transitions during payment simulation.
 * 3. Verify that official booking records are correctly mapped and persisted.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class FlightBookingConfirmViewModelTest {

    // Mocks for business logic and data persistence
    private val draftRepository: FlightBookingDraftRepository = mockk()
    private val createPaymentIntentUseCase: CreatePaymentIntentUseCase = mockk()
    private val flightBookingRepository: FlightBookingRepository = mockk()

    private lateinit var viewModel: FlightBookingConfirmViewModel
    private val testDispatcher = StandardTestDispatcher()

    // Professional Mock Data setup
    private val mockFlight = Flight(
        id = "f1", airline = Airline("LA", "Latam", ""), flightNumber = "LA123",
        origin = Airport("SCL", "Santiago", "", ""), destination = Airport("LIM", "Lima", "", ""),
        departureTime = 0, arrivalTime = 0, duration = 4.hours, cabinClass = CabinClass.ECONOMY
    )
    private val mockOffer = FlightOffer(id = "o1", flight = mockFlight, basePrice = 100.0)

    // A draft with 3 total passengers (2 Adults, 1 Infant)
    private val mockDraft = FlightBookingDraft(
        adults = 2, children = 0, infants = 1,
        selectedOffers = mapOf(0 to mockOffer)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Default success behaviors for repository and payment intent
        coEvery { draftRepository.getDraft() } returns flowOf(mockDraft)
        coEvery { flightBookingRepository.saveBooking(any()) } returns AppResult.Success(Unit)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Integrity Test: Checks if the ViewModel correctly computes the total price
     * based on the passenger count and flight offers.
     */
    @Test
    fun `when initialized, should calculate total price correctly for all passengers`() = runTest(testDispatcher) {
        viewModel = FlightBookingConfirmViewModel(draftRepository, createPaymentIntentUseCase, flightBookingRepository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        // Logic: (100.0 base price) * (2 adults + 1 infant) = 300
        assertEquals("Price calculation mismatch", 300, state.totalPrice)
        assertEquals("Currency should be USD", "USD", state.currency)
    }

    /**
     * Flow Test: Verifies that a successful payment simulation sets the
     * success flag for navigation.
     */
    @Test
    fun `when payment succeeds, should set navigation flag to true and clear loading`() = runTest(testDispatcher) {
        viewModel = FlightBookingConfirmViewModel(draftRepository, createPaymentIntentUseCase, flightBookingRepository)
        advanceUntilIdle()

        viewModel.onEvent(FlightBookingConfirmEvent.OnSimulateSuccessClick)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("isPaymentSuccessful flag should be true", state.isPaymentSuccessful)
        assertEquals("isLoading should be false after success", false, state.isLoading)
    }

    /**
     * Resilience Test: Ensures that if the repository save fails, the error is
     * correctly reported in the state.
     */
    @Test
    fun `when repository save fails, should update state with error message`() = runTest(testDispatcher) {
        coEvery { flightBookingRepository.saveBooking(any()) } returns AppResult.Failure(mockk())

        viewModel = FlightBookingConfirmViewModel(draftRepository, createPaymentIntentUseCase, flightBookingRepository)
        advanceUntilIdle()

        viewModel.onEvent(FlightBookingConfirmEvent.OnSimulateSuccessClick)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Error message should be present", "Failed to save record", state.error)
        assertEquals("Success flag should remain false", false, state.isPaymentSuccessful)
    }

    /**
     * Data Mapping Test: Critically verifies that the temporary draft is
     * correctly converted into an official FlightBooking object before saving.
     */
    @Test
    fun `when booking is officially saved, final object must contain correct confirmed data`() = runTest(testDispatcher) {
        viewModel = FlightBookingConfirmViewModel(draftRepository, createPaymentIntentUseCase, flightBookingRepository)
        advanceUntilIdle()

        viewModel.onEvent(FlightBookingConfirmEvent.OnSimulateSuccessClick)
        advanceUntilIdle()

        // Verify the mapping logic through coVerify
        coVerify {
            flightBookingRepository.saveBooking(withArg { booking ->
                assertEquals("Booking amount mismatch", 300.0, booking.totalAmount, 0.0)
                assertEquals("Confirmed status mismatch", BookingStatus.COMPLETED, booking.status)
                assertTrue("PNR code should follow standard format", booking.confirmationCode.startsWith("FL-"))
            })
        }
    }

    /**
     * UI State Test: Ensures the ViewModel can reset an error state.
     */
    @Test
    fun `when dismiss error is triggered, state error should become null`() = runTest(testDispatcher) {
        viewModel = FlightBookingConfirmViewModel(draftRepository, createPaymentIntentUseCase, flightBookingRepository)
        advanceUntilIdle()

        viewModel.onEvent(FlightBookingConfirmEvent.OnDismissError)

        val state = viewModel.uiState.value
        assertEquals("Error state not cleared", null, state.error)
    }
}