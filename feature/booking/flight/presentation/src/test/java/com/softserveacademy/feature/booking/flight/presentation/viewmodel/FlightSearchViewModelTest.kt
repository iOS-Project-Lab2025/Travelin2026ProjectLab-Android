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

/**
 * Unit tests for [FlightSearchViewModel].
 * Validates state transitions, event handling, and segment persistence logic.
 */
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
        // Default behavior for airport suggestions
        coEvery { searchAirportsUseCase(any()) } returns flowOf(emptyList())
        viewModel = FlightSearchViewModel(searchAirportsUseCase, validateUseCase, draftRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Verifies that the 'Swap' action correctly exchanges Origin and Destination codes.
     */
    @Test
    fun `when swap locations is triggered, origin and destination should exchange`() = runTest {
        viewModel.onEvent(FlightSearchEvent.OnOriginQueryChanged(0, "SCL"))
        viewModel.onEvent(FlightSearchEvent.OnDestinationQueryChanged(0, "LIM"))

        viewModel.onEvent(FlightSearchEvent.OnSwapSegmentLocations(0))

        val segment = viewModel.uiState.value.segments[0]
        assertEquals("LIM", segment.origin)
        assertEquals("SCL", segment.destination)
    }

    /**
     * Confirms that valid search criteria results in a call to the draft repository.
     */
    @Test
    fun `when performing search with valid data, it should save draft`() = runTest {
        val mockResult = ValidateFlightSearchUseCase.FlightValidationResult(isValid = true)
        coEvery { validateUseCase.validate(any(), any(), any()) } returns mockResult

        viewModel.onEvent(FlightSearchEvent.OnOriginQueryChanged(0, "SCL"))
        viewModel.onEvent(FlightSearchEvent.OnDestinationQueryChanged(0, "LIM"))

        viewModel.onEvent(FlightSearchEvent.OnPerformSearch)

        coVerify { draftRepository.saveDraft(any()) }
    }

    /**
     * Requirement US1: Verify that segments list cannot grow beyond 4 items.
     */
    @Test
    fun `when attempting to add more than 4 segments, the list size remains 4`() = runTest {
        // Add segments until reaching the limit
        repeat(5) { viewModel.onEvent(FlightSearchEvent.OnAddSegment) }

        assertEquals("Should not exceed 4 segments", 4, viewModel.uiState.value.segments.size)
    }

    /**
     * Confirms that airport suggestions are updated in the state when the user types.
     */
    @Test
    fun `when typing an airport, suggestions should be updated`() = runTest {
        val mockAirports = listOf(Airport("SCL", "Santiago", "SCL", "Chile"))
        coEvery { searchAirportsUseCase("SCL") } returns flowOf(mockAirports)

        viewModel.onEvent(FlightSearchEvent.OnOriginQueryChanged(0, "SCL"))

        assertEquals(mockAirports, viewModel.uiState.value.originSuggestions)
    }

    /**
     * Verifies that errors are automatically cleared when the user modifies the affected field.
     */
    @Test
    fun `when origin has error and user types again, error should be cleared`() = runTest {
        val mockError = mapOf(0 to ValidateFlightSearchUseCase.SegmentError(
            originError = ValidateFlightSearchUseCase.FlightError.INVALID_ORIGIN
        ))
        coEvery { validateUseCase.validate(any(), any(), any()) } returns ValidateFlightSearchUseCase.FlightValidationResult(segmentErrors = mockError, isValid = false)

        viewModel.onEvent(FlightSearchEvent.OnPerformSearch)
        assertEquals(ValidateFlightSearchUseCase.FlightError.INVALID_ORIGIN, viewModel.uiState.value.errors[0]?.originError)

        viewModel.onEvent(FlightSearchEvent.OnOriginQueryChanged(0, "S"))

        assertEquals(null, viewModel.uiState.value.errors[0]?.originError)
    }

    /**
     * Verifies round trip shows two segments with inverted routes.
     */

    @Test
    fun `when performing Round Trip search, it should generate two segments automatically`() = runTest {
        // 1. Setup as Round Trip
        viewModel.onEvent(FlightSearchEvent.OnFlightTypeSelected(FlightType.ROUND_TRIP))
        viewModel.onEvent(FlightSearchEvent.OnOriginQueryChanged(0, "SCL"))
        viewModel.onEvent(FlightSearchEvent.OnDestinationQueryChanged(0, "LIM"))

        val mockResult = ValidateFlightSearchUseCase.FlightValidationResult(isValid = true)
        coEvery { validateUseCase.validate(any(), any(), any()) } returns mockResult

        // 2. Trigger Search
        viewModel.onEvent(FlightSearchEvent.OnPerformSearch)

        // 3. Verify that the draft saved contains 2 segments with inverted routes
        coVerify {
            draftRepository.saveDraft(match { draft ->
                draft.segments.size == 2 &&
                        draft.segments[0].origin == "SCL" && draft.segments[0].destination == "LIM" &&
                        draft.segments[1].origin == "LIM" && draft.segments[1].destination == "SCL"
            })
        }
    }
}