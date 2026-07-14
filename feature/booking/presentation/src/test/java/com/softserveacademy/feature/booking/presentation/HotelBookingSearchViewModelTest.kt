package com.softserveacademy.feature.booking.presentation

import androidx.lifecycle.SavedStateHandle
import com.softserveacademy.feature.booking.domain.HotelBookingDraft
import com.softserveacademy.feature.booking.domain.ValidateBookingSearchUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HotelBookingSearchViewModelTest {

    private lateinit var viewModel: HotelBookingSearchViewModel
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var validateBookingSearchUseCase: ValidateBookingSearchUseCase

    @Before
    fun setUp() {
        savedStateHandle = SavedStateHandle()
        validateBookingSearchUseCase = ValidateBookingSearchUseCase()
        viewModel = HotelBookingSearchViewModel(savedStateHandle, validateBookingSearchUseCase)
    }

    @Test
    fun `initial state is default`() {
        val state = viewModel.uiState.value
        assertEquals(1, state.adultsCount)
        assertEquals(0, state.childrenCount)
        assertFalse(state.hasPets)
        assertNull(state.startDateMillis)
        assertNull(state.endDateMillis)
        assertFalse(state.showGuestBottomSheet)
    }

    @Test
    fun `restores state from SavedStateHandle`() {
        val draft = HotelBookingDraft(amountOfAdults = 3, checkInDate = 1000L)
        savedStateHandle["booking_draft"] = draft
        
        val newViewModel = HotelBookingSearchViewModel(savedStateHandle, validateBookingSearchUseCase)
        assertEquals(3, newViewModel.uiState.value.adultsCount)
        assertEquals(1000L, newViewModel.uiState.value.startDateMillis)
    }

    @Test
    fun `onDateRangeSelected updates state and savedState`() {
        viewModel.onEvent(TravelBookingSearchEvent.OnDateRangeSelected(100L, 200L))
        
        val state = viewModel.uiState.value
        assertEquals(100L, state.startDateMillis)
        assertEquals(200L, state.endDateMillis)
        
        val savedDraft = savedStateHandle.get<HotelBookingDraft>("booking_draft")
        assertEquals(100L, savedDraft?.checkInDate)
    }

    @Test
    fun `onNextClick shows error when dates are missing`() {
        viewModel.onEvent(TravelBookingSearchEvent.OnNextClick)
        
        val state = viewModel.uiState.value
        assertTrue(state.isDateErrorVisible)
        assertFalse(state.showGuestBottomSheet)
    }

    @Test
    fun `onNextClick shows bottom sheet when dates are selected`() {
        viewModel.onEvent(TravelBookingSearchEvent.OnDateRangeSelected(100L, 200L))
        viewModel.onEvent(TravelBookingSearchEvent.OnNextClick)
        
        val state = viewModel.uiState.value
        assertFalse(state.isDateErrorVisible)
        assertTrue(state.showGuestBottomSheet)
    }

    @Test
    fun `onAcceptGuests shows error when adults count is less than 1`() {
        viewModel.onEvent(TravelBookingSearchEvent.OnDateRangeSelected(100L, 200L))
        viewModel.onEvent(TravelBookingSearchEvent.OnNextClick)
        
        viewModel.onEvent(TravelBookingSearchEvent.OnAdultsCountChange(0))
        viewModel.onEvent(TravelBookingSearchEvent.OnAcceptGuests)
        
        val state = viewModel.uiState.value
        assertTrue(state.isGuestErrorVisible)
        assertTrue(state.showGuestBottomSheet)
    }

    @Test
    fun `onAcceptGuests dismisses sheet on success`() {
        viewModel.onEvent(TravelBookingSearchEvent.OnDateRangeSelected(100L, 200L))
        viewModel.onEvent(TravelBookingSearchEvent.OnNextClick)
        
        viewModel.onEvent(TravelBookingSearchEvent.OnAdultsCountChange(2))
        viewModel.onEvent(TravelBookingSearchEvent.OnAcceptGuests)
        
        val state = viewModel.uiState.value
        assertFalse(state.isGuestErrorVisible)
        assertFalse(state.showGuestBottomSheet)
    }
}
