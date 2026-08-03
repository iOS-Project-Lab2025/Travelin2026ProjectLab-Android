package com.softserveacademy.feature.booking.flight.presentation.viewmodel

import com.softserveacademy.core.domain.model.FlightOffer
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
import org.junit.Before
import org.junit.Test
import com.softserveacademy.feature.booking.flight.presentation.R

@OptIn(ExperimentalCoroutinesApi::class)
class FlightResultsViewModelTest {

    private val draftRepository: FlightBookingDraftRepository = mockk(relaxed = true)
    private val searchFlightsUseCase: SearchFlightsUseCase = mockk()

    private lateinit var viewModel: FlightResultsViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        // Mock of a valid draft so init will not fail
        coEvery { draftRepository.getDraft() } returns flowOf(mockk(relaxed = true))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when results are loaded, show only first 5 offers initially`() = runTest {
        // 10 mock flights
        val manyOffers = List(10) { mockk<FlightOffer>(relaxed = true) }
        coEvery { searchFlightsUseCase(any(), any(), any(), any(), any(), any()) } returns flowOf(manyOffers)

        viewModel = FlightResultsViewModel(draftRepository, searchFlightsUseCase)

        assertEquals(5, viewModel.uiState.value.visibleOffers.size)
        assertEquals(10, viewModel.uiState.value.totalAvailableCount)
    }

    @Test
    fun `when Load More is triggered, increase visible offers count`() = runTest {
        val manyOffers = List(15) { mockk<FlightOffer>(relaxed = true) }
        coEvery { searchFlightsUseCase(any(), any(), any(), any(), any(), any()) } returns flowOf(manyOffers)

        viewModel = FlightResultsViewModel(draftRepository, searchFlightsUseCase)
        viewModel.onEvent(FlightResultsEvent.OnLoadMore)

        assertEquals(10, viewModel.uiState.value.visibleOffers.size)
    }

    @Test
    fun `when network fails, show network error resource`() = runTest {
        coEvery { searchFlightsUseCase(any(), any(), any(), any(), any(), any()) } returns kotlinx.coroutines.flow.flow {
            throw java.io.IOException("No internet")
        }

        viewModel = FlightResultsViewModel(draftRepository, searchFlightsUseCase)

        assertEquals(R.string.flight_error_network, viewModel.uiState.value.error)
        assertEquals(0, viewModel.uiState.value.visibleOffers.size)
    }
}