package com.softserveacademy.home.presentation.viewmodel

import com.softserveacademy.core.domain.model.Trip
import com.softserveacademy.home.domain.usecases.GetUpcomingTripUseCase
import com.softserveacademy.home.presentation.state.SectionState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UpcomingTripViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val getUpcomingTripUseCase = mockk<GetUpcomingTripUseCase>()
    private lateinit var viewModel: UpcomingTripViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given trip exists when loadTrip then state is Success`() = runTest {
        val trip = mockk<Trip>()
        coEvery { getUpcomingTripUseCase() } returns Result.success(trip)

        viewModel = UpcomingTripViewModel(getUpcomingTripUseCase)
        viewModel.loadTrip()

        val state = viewModel.state.value
        assertTrue(state.trip is SectionState.Success)
        assertEquals(trip, (state.trip as SectionState.Success).data)
    }

    @Test
    fun `given no trip when loadTrip then state is Empty`() = runTest {
        coEvery { getUpcomingTripUseCase() } returns Result.success(null)

        viewModel = UpcomingTripViewModel(getUpcomingTripUseCase)
        viewModel.loadTrip()

        val state = viewModel.state.value
        assertTrue(state.trip is SectionState.Empty)
    }

    @Test
    fun `given error when loadTrip then state is Error`() = runTest {
        coEvery { getUpcomingTripUseCase() } returns Result.failure(Exception("Trip error"))

        viewModel = UpcomingTripViewModel(getUpcomingTripUseCase)
        viewModel.loadTrip()

        val state = viewModel.state.value
        assertTrue(state.trip is SectionState.Error)
        assertEquals("Trip error", (state.trip as SectionState.Error).message)
    }

    @Test
    fun `given error with null message when loadTrip then state is Error with default message`() = runTest {
        coEvery { getUpcomingTripUseCase() } returns Result.failure(Exception())

        viewModel = UpcomingTripViewModel(getUpcomingTripUseCase)
        viewModel.loadTrip()

        val state = viewModel.state.value
        assertTrue(state.trip is SectionState.Error)
        assertEquals("Failed to load trip", (state.trip as SectionState.Error).message)
    }
}
