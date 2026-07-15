package com.softserveacademy.feature.booking.presentation

import androidx.lifecycle.SavedStateHandle
import com.softserveacademy.feature.booking.domain.repository.BookingRepository
import com.softserveacademy.feature.booking.domain.model.HotelBookingDraft
import com.softserveacademy.feature.booking.domain.ValidateBookingSearchUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HotelBookingSearchViewModelTest {

    private lateinit var viewModel: HotelBookingSearchViewModel
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var validateBookingSearchUseCase: ValidateBookingSearchUseCase
    private lateinit var bookingRepository: BookingRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle()
        validateBookingSearchUseCase = ValidateBookingSearchUseCase()
        bookingRepository = mockk(relaxed = true)
        viewModel = HotelBookingSearchViewModel(savedStateHandle, validateBookingSearchUseCase, bookingRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is default`() = runTest {
        val state = viewModel.uiState.value
        assertEquals(1, state.adultsCount)
        assertEquals(0, state.childrenCount)
        assertFalse(state.hasPets)
        assertNull(state.startDateMillis)
        assertNull(state.endDateMillis)
        assertFalse(state.showGuestBottomSheet)
    }

    @Test
    fun `restores state from SavedStateHandle`() = runTest {
        val draft = HotelBookingDraft(amountOfAdults = 3, checkInDate = 1000L)
        savedStateHandle["booking_draft"] = draft
        
        val newViewModel = HotelBookingSearchViewModel(savedStateHandle, validateBookingSearchUseCase, bookingRepository)
        assertEquals(3, newViewModel.uiState.value.adultsCount)
        assertEquals(1000L, newViewModel.uiState.value.startDateMillis)
    }

    @Test
    fun `restores state from BookingRepository if SavedStateHandle is empty`() = runTest {
        val hotelIdInt = 123
        val draft = HotelBookingDraft(hotelId = hotelIdInt.toString(), amountOfAdults = 4)
        coEvery { bookingRepository.getHotelBookingDraft(any()) } returns draft
        
        val freshSavedStateHandle = SavedStateHandle(mapOf("hotelId" to hotelIdInt))
        val newViewModel = HotelBookingSearchViewModel(freshSavedStateHandle, validateBookingSearchUseCase, bookingRepository)
        
        advanceUntilIdle()
        
        assertEquals(4, newViewModel.uiState.value.adultsCount)
    }

    @Test
    fun `onDateRangeSelected updates state and savedState and repository`() = runTest {
        viewModel.onEvent(TravelBookingSearchEvent.OnDateRangeSelected(100L, 200L))
        
        val state = viewModel.uiState.value
        assertEquals(100L, state.startDateMillis)
        assertEquals(200L, state.endDateMillis)
        
        val savedDraft = savedStateHandle.get<HotelBookingDraft>("booking_draft")
        assertEquals(100L, savedDraft?.checkInDate)
        
        advanceUntilIdle()
        coVerify { bookingRepository.saveHotelBookingDraft(any()) }
    }

    @Test
    fun `onNextClick shows error when dates are missing`() = runTest {
        viewModel.onEvent(TravelBookingSearchEvent.OnNextClick)
        
        val state = viewModel.uiState.value
        assertTrue(state.isDateErrorVisible)
        assertFalse(state.showGuestBottomSheet)
    }

    @Test
    fun `onNextClick shows bottom sheet when dates are selected`() = runTest {
        viewModel.onEvent(TravelBookingSearchEvent.OnDateRangeSelected(100L, 200L))
        viewModel.onEvent(TravelBookingSearchEvent.OnNextClick)
        
        val state = viewModel.uiState.value
        assertFalse(state.isDateErrorVisible)
        assertTrue(state.showGuestBottomSheet)
    }

    @Test
    fun `onAcceptGuests shows error when adults count is less than 1`() = runTest {
        viewModel.onEvent(TravelBookingSearchEvent.OnDateRangeSelected(100L, 200L))
        viewModel.onEvent(TravelBookingSearchEvent.OnNextClick)
        
        viewModel.onEvent(TravelBookingSearchEvent.OnAdultsCountChange(0))
        viewModel.onEvent(TravelBookingSearchEvent.OnAcceptGuests)
        
        val state = viewModel.uiState.value
        assertTrue(state.isGuestErrorVisible)
        assertTrue(state.showGuestBottomSheet)
    }

    @Test
    fun `onAcceptGuests dismisses sheet on success`() = runTest {
        viewModel.onEvent(TravelBookingSearchEvent.OnDateRangeSelected(100L, 200L))
        viewModel.onEvent(TravelBookingSearchEvent.OnNextClick)
        
        viewModel.onEvent(TravelBookingSearchEvent.OnAdultsCountChange(2))
        viewModel.onEvent(TravelBookingSearchEvent.OnAcceptGuests)
        
        val state = viewModel.uiState.value
        assertFalse(state.isGuestErrorVisible)
        assertFalse(state.showGuestBottomSheet)
    }
}
