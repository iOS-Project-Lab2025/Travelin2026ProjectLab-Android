package com.softserveacademy.home.presentation.viewmodel

import com.softserveacademy.core.domain.model.AppTheme
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.domain.usecase.GetThemeUseCase
import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.home.presentation.state.HotelDetailState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [HotelDetailsViewModel] mocking error states.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HotelDetailsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val hotelRepo = mockk<HotelRepo>()
    private val getThemeUseCase = mockk<GetThemeUseCase>()
    private lateinit var viewModel: HotelDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getThemeUseCase() } returns flowOf(AppTheme.SYSTEM)
        viewModel = HotelDetailsViewModel(hotelRepo, getThemeUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given error when getHotelDetail is called then state is updated to Error`() = runTest {
        coEvery { hotelRepo.getHotelById(any()) } returns AppResult.Failure(AppError.Unknown(Exception("Network Error")))

        viewModel.getHotelDetail(1)

        advanceUntilIdle()
        val currentState = viewModel.hotelDetailState.value
        assertTrue("State should be HotelDetailState.Error", currentState is HotelDetailState.Error)
    }

    @Test
    fun `given null message exception when getHotelDetail is called then state is updated to Error with null message`() = runTest {
        coEvery { hotelRepo.getHotelById(any()) } returns AppResult.Failure(AppError.Unknown(Exception()))

        viewModel.getHotelDetail(1)

        advanceUntilIdle()
        val currentState = viewModel.hotelDetailState.value
        assertTrue(currentState is HotelDetailState.Error)
    }
}
