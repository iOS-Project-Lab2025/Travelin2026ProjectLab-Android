package com.softserveacademy.feature.booking.flight.presentation.viewmodel

import com.softserveacademy.core.domain.model.*
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingDraftRepository
import com.softserveacademy.feature.booking.flight.domain.usecase.SearchAirportsUseCase
import com.softserveacademy.feature.booking.flight.domain.usecase.ValidateFlightSearchUseCase
import com.softserveacademy.feature.booking.flight.presentation.events.FlightSearchEvent
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

@OptIn(ExperimentalCoroutinesApi::class)
class FlightSearchViewModelTest {

    private val searchAirportsUseCase: SearchAirportsUseCase = mockk()
    private val validateUseCase: ValidateFlightSearchUseCase = mockk()
    private val draftRepository: FlightBookingDraftRepository = mockk(relaxed = true)

    private lateinit var viewModel: FlightSearchViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        // Define a behavior by default to avoid non-configured errors
        coEvery { searchAirportsUseCase(any()) } returns flowOf(emptyList())
        viewModel = FlightSearchViewModel(searchAirportsUseCase, validateUseCase, draftRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when swap locations is triggered, origin and destination should exchange`() = runTest {
        viewModel.onEvent(FlightSearchEvent.OnOriginQueryChanged(0, "SCL"))
        viewModel.onEvent(FlightSearchEvent.OnDestinationQueryChanged(0, "LIM"))

        viewModel.onEvent(FlightSearchEvent.OnSwapSegmentLocations(0))

        val segment = viewModel.uiState.value.segments[0]
        assertEquals("LIM", segment.origin)
        assertEquals("SCL", segment.destination)
    }

    @Test
    fun `when performing search with valid data, it should save draft`() = runTest {
        // Setup: Mock successful validation
        val mockResult = ValidateFlightSearchUseCase.FlightValidationResult(isValid = true)
        coEvery { validateUseCase.validate(any(), any(), any()) } returns mockResult

        viewModel.onEvent(FlightSearchEvent.OnOriginQueryChanged(0, "SCL"))
        viewModel.onEvent(FlightSearchEvent.OnDestinationQueryChanged(0, "LIM"))

        viewModel.onEvent(FlightSearchEvent.OnPerformSearch)

        // Verify that the draft repository was called (even without to import the exact content of miliseconds)
        coVerify { draftRepository.saveDraft(any()) }
    }

    @Test
    fun `when typing an airport, suggestions should be updated`() = runTest {
        val mockAirports = listOf(Airport("SCL", "Santiago", "SCL", "Chile"))
        // Configure specific mock behavior
        coEvery { searchAirportsUseCase("SCL") } returns flowOf(mockAirports)

        viewModel.onEvent(FlightSearchEvent.OnOriginQueryChanged(0, "SCL"))

        // The UnconfinedTestDispatcher will process the Flow's emissions immediately
        assertEquals(mockAirports, viewModel.uiState.value.originSuggestions)
    }

    @Test
    fun `when selecting a suggestion, query should be updated and suggestions cleared`() = runTest {
        val airport = Airport("JFK", "New York", "NYC", "USA")

        viewModel.onEvent(FlightSearchEvent.OnOriginSelected(0, airport))

        assertEquals("JFK", viewModel.uiState.value.segments[0].origin)
        assertTrue("Suggestions should be empty after selection", viewModel.uiState.value.originSuggestions.isEmpty())
    }

    @Test
    fun `when passenger count changes, state should update correctly`() = runTest {
        viewModel.onEvent(FlightSearchEvent.OnAdultsChanged(3))
        assertEquals(3, viewModel.uiState.value.adults)
    }

    @Test
    fun `when origin has error and user types again, error should be cleared`() = runTest {
        // 1. Foce an initial error state
        val mockError = mapOf(0 to ValidateFlightSearchUseCase.SegmentError(originError = ValidateFlightSearchUseCase.FlightError.INVALID_ORIGIN))
        coEvery { validateUseCase.validate(any(), any(), any()) } returns ValidateFlightSearchUseCase.FlightValidationResult(segmentErrors = mockError, isValid = false)

        viewModel.onEvent(FlightSearchEvent.OnPerformSearch)
        assertEquals(ValidateFlightSearchUseCase.FlightError.INVALID_ORIGIN, viewModel.uiState.value.errors[0]?.originError)

        // 2. Action: User writes to the origin
        viewModel.onEvent(FlightSearchEvent.OnOriginQueryChanged(0, "S"))

        // 3. Verifying origin error is clenead
        assertEquals(null, viewModel.uiState.value.errors[0]?.originError)
    }
}