package com.softserveacademy.home.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.softserveacademy.core.domain.usecase.GetNearbyPlacesUseCase
import com.softserveacademy.core.domain.usecase.GetNearbyRestaurantsUseCase
import com.softserveacademy.core.domain.usecase.GetNearbyTransportUseCase
import com.softserveacademy.core.domain.usecase.hotel.GetHotelDetailsUseCase
import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.AppResult
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
    private val getNearbyPlacesUseCase = mockk<GetNearbyPlacesUseCase>(relaxed = true)
    private val getNearbyTransportUseCase = mockk<GetNearbyTransportUseCase>(relaxed = true)
    private val getNearbyRestaurantsUseCase = mockk<GetNearbyRestaurantsUseCase>(relaxed = true)
    private lateinit var viewModel: HotelDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HotelDetailsViewModel(
            savedStateHandle = SavedStateHandle(),
            getHotelDetailsUseCase = getHotelDetailsUseCase,
            getNearbyPlacesUseCase = getNearbyPlacesUseCase,
            getNearbyTransportUseCase = getNearbyTransportUseCase,
            getNearbyRestaurantsUseCase = getNearbyRestaurantsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given error when Load event is triggered then state is updated with errorMessage`() = runTest {
        // GIVEN: The use case returns a failure
        val errorMessage = "Network Error"
        coEvery { getHotelDetailsUseCase(any()) } returns AppResult.Failure(AppError.Unknown(Exception(errorMessage)))

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
        coEvery { getHotelDetailsUseCase(any()) } returns AppResult.Failure(AppError.Unknown(Exception()))

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
