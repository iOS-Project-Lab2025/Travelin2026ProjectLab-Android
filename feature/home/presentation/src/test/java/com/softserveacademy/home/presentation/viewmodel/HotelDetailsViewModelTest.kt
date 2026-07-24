package com.softserveacademy.home.presentation.viewmodel

import com.softserveacademy.core.domain.model.AppTheme
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.domain.usecase.GetThemeUseCase
import com.softserveacademy.home.presentation.state.HotelDetailState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
        // GIVEN: The repository throws an exception
        val errorMessage = "Network Error"
        coEvery { hotelRepo.getHotelById(any()) } throws Exception(errorMessage)

        // WHEN: getHotelDetail is called
        viewModel.getHotelDetail(1)

        // THEN: The state should be Error with the expected message
        val currentState = viewModel.hotelDetailState.value
        assertTrue("State should be HotelDetailState.Error", currentState is HotelDetailState.Error)
        assertEquals(errorMessage, (currentState as HotelDetailState.Error).message)
    }

    @Test
    fun `given null message exception when getHotelDetail is called then state is updated to Error with null message`() = runTest {
        // GIVEN: The repository throws an exception without a message
        coEvery { hotelRepo.getHotelById(any()) } throws Exception()

        // WHEN: getHotelDetail is called
        viewModel.getHotelDetail(1)

        // THEN: The state should be Error
        val currentState = viewModel.hotelDetailState.value
        assertTrue(currentState is HotelDetailState.Error)
        assertEquals(null, (currentState as HotelDetailState.Error).message)
    }
}
