package com.softserveacademy.feature.booking.flight.presentation.viewmodel

import com.softserveacademy.core.domain.model.*
import com.softserveacademy.feature.booking.flight.domain.model.FlightBookingDraft
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingDraftRepository
import com.softserveacademy.feature.booking.flight.domain.usecase.SearchFlightsUseCase
import com.softserveacademy.feature.booking.flight.presentation.events.FlightResultsEvent
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import com.softserveacademy.feature.booking.flight.presentation.R

/**
 * Unit tests for [FlightResultsViewModel].
 * Validates search fetching, iterative segment selection, and technical error handling.
 *
 * Ensures the iterative selection flow (US2) and recovery via retry (US1)
 * behave correctly under different network conditions.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class FlightResultsViewModelTest {

    private val draftRepository: FlightBookingDraftRepository = mockk(relaxed = true)
    private val searchFlightsUseCase: SearchFlightsUseCase = mockk()

    private lateinit var viewModel: FlightResultsViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    /**
     * Real draft instance to ensure the ViewModel doesn't crash during init
     * when accessing segment list indices.
     */
    private val mockDraft = FlightBookingDraft(
        segments = listOf(FlightSegment(origin = "SCL", destination = "LIM"))
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Setup initial draft state with a valid segment to satisfy initialization logic
        coEvery { draftRepository.getDraft() } returns flowOf(mockDraft)

        // Default empty list for search calls
        coEvery { searchFlightsUseCase(any(), any(), any(), any(), any(), any()) } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Confirms that pagination logic works by showing exactly 5 offers at the start
     * regardless of how many results the server returns.
     */
    @Test
    fun `when results are loaded, show only first 5 offers initially`() = runTest {
        // Setup: Server returns 10 matching flights
        val manyOffers = List(10) { mockk<FlightOffer>(relaxed = true) }
        coEvery { searchFlightsUseCase(any(), any(), any(), any(), any(), any()) } returns flowOf(manyOffers)

        viewModel = FlightResultsViewModel(draftRepository, searchFlightsUseCase)

        // Verifications
        assertEquals("Should only show 5 visible offers initially", 5, viewModel.uiState.value.visibleOffers.size)
        assertEquals("Total available count should be accurate", 10, viewModel.uiState.value.totalAvailableCount)
    }

    /**
     * Requirement US2: Verify that selecting a flight only updates the visual state
     * and does not trigger automatic navigation.
     */
    @Test
    fun `when flight is selected, update selectedOfferId in state for UI highlighting`() = runTest {
        // Setup: Define a flight with a specific ID
        val mockOffer = mockk<FlightOffer>(relaxed = true) { coEvery { id } returns "FL-123" }
        coEvery { searchFlightsUseCase(any(), any(), any(), any(), any(), any()) } returns flowOf(listOf(mockOffer))

        viewModel = FlightResultsViewModel(draftRepository, searchFlightsUseCase)

        // Action: User clicks the flight
        viewModel.onEvent(FlightResultsEvent.OnFlightSelected("FL-123"))

        // Verification
        assertEquals("ID should be tracked in the state", "FL-123", viewModel.uiState.value.selectedOfferId)
    }

    /**
     * Requirement US1: Verify that network failures (IOException) update the state
     * with the correct technical error resource.
     */
    @Test
    fun `when network fails, show network error resource`() = runTest {
        // Setup: Flow that throws an IOException
        coEvery { searchFlightsUseCase(any(), any(), any(), any(), any(), any()) } returns kotlinx.coroutines.flow.flow {
            throw java.io.IOException("No internet connection")
        }

        viewModel = FlightResultsViewModel(draftRepository, searchFlightsUseCase)

        // Verification
        assertEquals("State should contain the network error ID", R.string.flight_error_network, viewModel.uiState.value.error)
    }

    /**
     * Verifies that the retry action (US1) successfully clears existing errors
     * and restarts the search process.
     */
    @Test
    fun `when retry is triggered, clear error and reload search from repository`() = runTest {
        // 1. Initial state with a network failure
        coEvery { searchFlightsUseCase(any(), any(), any(), any(), any(), any()) } returns kotlinx.coroutines.flow.flow {
            throw java.io.IOException()
        }
        viewModel = FlightResultsViewModel(draftRepository, searchFlightsUseCase)
        assertEquals(R.string.flight_error_network, viewModel.uiState.value.error)

        // 2. Setup a successful response for the retry call
        coEvery { searchFlightsUseCase(any(), any(), any(), any(), any(), any()) } returns flowOf(emptyList())

        // Action: User clicks Retry
        viewModel.onEvent(FlightResultsEvent.OnRetryClick)

        // Verification
        assertNull("Error state should be cleared after successful retry", viewModel.uiState.value.error)
    }
}