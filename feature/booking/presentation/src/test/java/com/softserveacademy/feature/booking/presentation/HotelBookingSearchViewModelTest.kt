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
        assertEquals(1, state.draft.amountOfAdults)
        assertEquals(0, state.draft.amountOfChildren)
        assertEquals(0, state.draft.amountOfPets)
        assertNull(state.draft.checkInDate)
        assertNull(state.draft.checkOutDate)
        assertFalse(state.showGuestBottomSheet)
    }

    @Test
    fun `restores state from SavedStateHandle`() {
        val draft = HotelBookingDraft(amountOfAdults = 3, checkInDate = 1000L)
        savedStateHandle["booking_draft"] = draft
        
        val newViewModel = HotelBookingSearchViewModel(savedStateHandle, validateBookingSearchUseCase)
        assertEquals(3, newViewModel.uiState.value.draft.amountOfAdults)
        assertEquals(1000L, newViewModel.uiState.value.draft.checkInDate)
    }

    @Test
    fun `onDateRangeSelected updates state and savedState`() {
        viewModel.onEvent(HotelBookingSearchEvent.OnDateRangeSelected(100L, 200L))
        
        val state = viewModel.uiState.value
        assertEquals(100L, state.draft.checkInDate)
        assertEquals(200L, state.draft.checkOutDate)
        
        val savedDraft = savedStateHandle.get<HotelBookingDraft>("booking_draft")
        assertEquals(100L, savedDraft?.checkInDate)
    }

    @Test
    fun `onNextClick shows error when dates are missing`() {
        viewModel.onEvent(HotelBookingSearchEvent.OnNextClick)
        
        val state = viewModel.uiState.value
        assertTrue(state.isDateErrorVisible)
        assertFalse(state.showGuestBottomSheet)
    }

    @Test
    fun `onNextClick shows bottom sheet when dates are selected`() {
        viewModel.onEvent(HotelBookingSearchEvent.OnDateRangeSelected(100L, 200L))
        viewModel.onEvent(HotelBookingSearchEvent.OnNextClick)
        
        val state = viewModel.uiState.value
        assertFalse(state.isDateErrorVisible)
        assertTrue(state.showGuestBottomSheet)
    }

    @Test
    fun `onAcceptGuests shows error when adults count is less than 1`() {
        viewModel.onEvent(HotelBookingSearchEvent.OnDateRangeSelected(100L, 200L))
        viewModel.onEvent(HotelBookingSearchEvent.OnNextClick)
        
        viewModel.onEvent(HotelBookingSearchEvent.OnAdultsCountChange(0))
        viewModel.onEvent(HotelBookingSearchEvent.OnAcceptGuests)
        
        val state = viewModel.uiState.value
        assertTrue(state.isGuestErrorVisible)
        assertTrue(state.showGuestBottomSheet)
    }

    @Test
    fun `onAcceptGuests dismisses sheet on success`() {
        viewModel.onEvent(HotelBookingSearchEvent.OnDateRangeSelected(100L, 200L))
        viewModel.onEvent(HotelBookingSearchEvent.OnNextClick)
        
        viewModel.onEvent(HotelBookingSearchEvent.OnAdultsCountChange(2))
        viewModel.onEvent(HotelBookingSearchEvent.OnAcceptGuests)
        
        val state = viewModel.uiState.value
        assertFalse(state.isGuestErrorVisible)
        assertFalse(state.showGuestBottomSheet)
    }
}
