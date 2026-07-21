package com.softserveacademy.feature.booking.common.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.softserveacademy.feature.booking.common.domain.repository.BookingRepository
import com.softserveacademy.feature.booking.common.domain.model.HotelBookingDraft
import com.softserveacademy.feature.booking.common.domain.usecase.ValidateEnterBookingDetailsUseCase
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent
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
class HotelEnterBookingDetailsViewModelTest {

    private lateinit var viewModel: HotelEnterBookingDetailsViewModel
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var validateEnterBookingDetailsUseCase: ValidateEnterBookingDetailsUseCase
    private lateinit var bookingRepository: BookingRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle()
        validateEnterBookingDetailsUseCase = ValidateEnterBookingDetailsUseCase()
        bookingRepository = mockk(relaxed = true)
        viewModel = HotelEnterBookingDetailsViewModel(savedStateHandle, validateEnterBookingDetailsUseCase, bookingRepository)
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
        
        val newViewModel = HotelEnterBookingDetailsViewModel(savedStateHandle, validateEnterBookingDetailsUseCase, bookingRepository)
        assertEquals(3, newViewModel.uiState.value.adultsCount)
        assertEquals(1000L, newViewModel.uiState.value.startDateMillis)
    }

    @Test
    fun `restores state from BookingRepository if SavedStateHandle is empty`() = runTest {
        val hotelIdInt = 123
        val draft = HotelBookingDraft(hotelId = hotelIdInt.toString(), amountOfAdults = 4)
        coEvery { bookingRepository.getHotelBookingDraft(any()) } returns draft
        
        val freshSavedStateHandle = SavedStateHandle(mapOf("hotelId" to hotelIdInt))
        val newViewModel = HotelEnterBookingDetailsViewModel(freshSavedStateHandle, validateEnterBookingDetailsUseCase, bookingRepository)
        
        advanceUntilIdle()
        
        assertEquals(4, newViewModel.uiState.value.adultsCount)
    }

    @Test
    fun `onDateRangeSelected updates state and savedState and repository`() = runTest {
        viewModel.onEvent(TravelEnterBookingDetailsEvent.OnDateRangeSelected(100L, 200L))
        
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
        viewModel.onEvent(TravelEnterBookingDetailsEvent.OnNextClick)
        
        val state = viewModel.uiState.value
        assertTrue(state.isDateErrorVisible)
        assertFalse(state.showGuestBottomSheet)
    }

    @Test
    fun `onNextClick shows bottom sheet when dates are selected`() = runTest {
        viewModel.onEvent(TravelEnterBookingDetailsEvent.OnDateRangeSelected(100L, 200L))
        viewModel.onEvent(TravelEnterBookingDetailsEvent.OnNextClick)
        
        val state = viewModel.uiState.value
        assertFalse(state.isDateErrorVisible)
        assertTrue(state.showGuestBottomSheet)
    }

    @Test
    fun `onAcceptClick shows error when adults count is less than 1`() = runTest {
        viewModel.onEvent(TravelEnterBookingDetailsEvent.OnDateRangeSelected(100L, 200L))
        viewModel.onEvent(TravelEnterBookingDetailsEvent.OnNextClick)
        
        viewModel.onEvent(TravelEnterBookingDetailsEvent.OnAdultsCountChange(0))
        viewModel.onEvent(TravelEnterBookingDetailsEvent.OnAcceptClick)
        
        val state = viewModel.uiState.value
        assertTrue(state.isGuestErrorVisible)
        assertTrue(state.showGuestBottomSheet)
    }

    @Test
    fun `onAcceptClick dismisses sheet and sets validation success on success`() = runTest {
        viewModel.onEvent(TravelEnterBookingDetailsEvent.OnDateRangeSelected(100L, 200L))
        viewModel.onEvent(TravelEnterBookingDetailsEvent.OnNextClick)
        
        viewModel.onEvent(TravelEnterBookingDetailsEvent.OnAdultsCountChange(2))
        viewModel.onEvent(TravelEnterBookingDetailsEvent.OnAcceptClick)
        
        val state = viewModel.uiState.value
        assertFalse(state.isGuestErrorVisible)
        assertFalse(state.showGuestBottomSheet)
        assertTrue(viewModel.validationSuccess.value)
    }

    @Test
    fun `resetValidationStatus sets validation success to false`() = runTest {
        viewModel.onEvent(TravelEnterBookingDetailsEvent.OnDateRangeSelected(100L, 200L))
        viewModel.onEvent(TravelEnterBookingDetailsEvent.OnNextClick)
        viewModel.onEvent(TravelEnterBookingDetailsEvent.OnAdultsCountChange(2))
        viewModel.onEvent(TravelEnterBookingDetailsEvent.OnAcceptClick)
        
        assertTrue(viewModel.validationSuccess.value)
        
        viewModel.resetValidationStatus()
        assertFalse(viewModel.validationSuccess.value)
    }
}
