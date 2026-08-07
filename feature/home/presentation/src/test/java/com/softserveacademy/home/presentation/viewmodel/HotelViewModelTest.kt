package com.softserveacademy.home.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.softserveacademy.core.domain.usecase.hotel.GetHotelDetailsUseCase
import com.softserveacademy.home.presentation.events.HotelDetailsEvent
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [HotelDetailsViewModel] mocking error states.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HotelViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val getHotelDetailsUseCase = mockk<GetHotelDetailsUseCase>()
    private lateinit var viewModel: HotelDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HotelDetailsViewModel(SavedStateHandle(), getHotelDetailsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given error when Load event is triggered then state is updated with errorMessage`() = runTest {
        // GIVEN: The use case returns a failure
        val errorMessage = "Network Error"
        coEvery { getHotelDetailsUseCase(any()) } returns Result.failure(Exception(errorMessage))

        // WHEN: Load event is triggered
        viewModel.onEvent(HotelDetailsEvent.Load("1"))

        advanceUntilIdle()
        // THEN: The state should have the expected error message and not be loading
        val currentState = viewModel.hotelDetailsState.value
        assertFalse(currentState.isLoading)
        assertEquals(errorMessage, currentState.errorMessage)
        assertNull(currentState.hotel)
    }

    @Test
    fun `given exception without message when Load event is triggered then state is updated with null errorMessage`() = runTest {
        // GIVEN: The use case returns a failure without a message
        coEvery { getHotelDetailsUseCase(any()) } returns Result.failure(Exception())

        // WHEN: Load event is triggered
        viewModel.onEvent(HotelDetailsEvent.Load("1"))

        advanceUntilIdle()
        // THEN: The state should have a null error message and not be loading
        val currentState = viewModel.hotelDetailsState.value
        assertFalse(currentState.isLoading)
        assertNull(currentState.errorMessage)
        assertNull(currentState.hotel)
    }
}
