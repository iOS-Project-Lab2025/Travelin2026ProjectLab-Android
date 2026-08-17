package com.softserveacademy.feature.booking.hotel.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.softserveacademy.feature.booking.hotel.domain.usecase.GetHotelBookingDraftUseCase
import com.softserveacademy.feature.booking.hotel.domain.usecase.SaveHotelBookingDraftUseCase
import com.softserveacademy.feature.booking.hotel.domain.model.HotelBookingDraft
import com.softserveacademy.feature.booking.hotel.domain.model.Guests
import com.softserveacademy.feature.booking.common.domain.usecase.ValidateEnterBookingDetailsUseCase
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent
import com.softserveacademy.feature.booking.common.presentation.states.TravelEnterBookingDetailsState
import com.softserveacademy.feature.booking.hotel.presentation.events.HotelEnterBookingDetailsEvent
import com.softserveacademy.feature.booking.hotel.presentation.states.HotelEnterBookingDetailsState
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
    private lateinit var getHotelBookingDraftUseCase: GetHotelBookingDraftUseCase
    private lateinit var saveHotelBookingDraftUseCase: SaveHotelBookingDraftUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val hotelId = "123"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle(mapOf("hotelId" to hotelId))
        validateEnterBookingDetailsUseCase = ValidateEnterBookingDetailsUseCase()
        getHotelBookingDraftUseCase = mockk(relaxed = true)
        saveHotelBookingDraftUseCase = mockk(relaxed = true)
        coEvery { getHotelBookingDraftUseCase(any()) } returns null
        viewModel = HotelEnterBookingDetailsViewModel(
            savedStateHandle,
            validateEnterBookingDetailsUseCase,
            getHotelBookingDraftUseCase,
            saveHotelBookingDraftUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is default`() = runTest {
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertEquals(1, state.adultsCount)
        assertEquals(0, state.childrenCount)
        assertFalse(state.hasPets)
        assertNull(state.screenState.startDateMillis)
        assertNull(state.screenState.endDateMillis)
        assertFalse(state.screenState.showGuestBottomSheet)
    }

    @Test
    fun `restores state from SavedStateHandle`() = runTest {
        val state = HotelEnterBookingDetailsState(
            adultsCount = 3,
            screenState = TravelEnterBookingDetailsState(startDateMillis = 1000L)
        )
        val freshSavedStateHandle = SavedStateHandle(mapOf(
            "hotelId" to hotelId,
            "booking_details_state" to state
        ))
        
        val newViewModel = HotelEnterBookingDetailsViewModel(
            freshSavedStateHandle,
            validateEnterBookingDetailsUseCase,
            getHotelBookingDraftUseCase,
            saveHotelBookingDraftUseCase
        )
        assertEquals(3, newViewModel.uiState.value.adultsCount)
        assertEquals(1000L, newViewModel.uiState.value.screenState.startDateMillis)
    }

    @Test
    fun `restores state from BookingRepository if SavedStateHandle is empty`() = runTest {
        val hotelId = "123"
        val draft = HotelBookingDraft(hotelId = hotelId, guests = Guests(adults = 4))
        coEvery { getHotelBookingDraftUseCase(any()) } returns draft
        
        val freshSavedStateHandle = SavedStateHandle(mapOf("hotelId" to hotelId))
        val newViewModel = HotelEnterBookingDetailsViewModel(
            freshSavedStateHandle,
            validateEnterBookingDetailsUseCase,
            getHotelBookingDraftUseCase,
            saveHotelBookingDraftUseCase
        )
        
        advanceUntilIdle()
        
        assertEquals(4, newViewModel.uiState.value.adultsCount)
    }

    @Test
    fun `onDateRangeSelected updates state and savedState and repository`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(HotelEnterBookingDetailsEvent.ScreenEvent(TravelEnterBookingDetailsEvent.OnDateRangeSelected(100L, 200L)))
        
        val state = viewModel.uiState.value
        assertEquals(100L, state.screenState.startDateMillis)
        assertEquals(200L, state.screenState.endDateMillis)
        
        val savedDraft = savedStateHandle.get<HotelBookingDraft>("hotel_booking_draft")
        assertEquals(100L, savedDraft?.checkIn)
        
        advanceUntilIdle()
        coVerify { saveHotelBookingDraftUseCase(any()) }
    }

    @Test
    fun `onNextClick shows error when dates are missing`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(HotelEnterBookingDetailsEvent.ScreenEvent(TravelEnterBookingDetailsEvent.OnNextClick))
        
        val state = viewModel.uiState.value
        assertTrue(state.screenState.isDateErrorVisible)
        assertFalse(state.screenState.showGuestBottomSheet)
    }

    @Test
    fun `onNextClick shows bottom sheet when dates are selected`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(HotelEnterBookingDetailsEvent.ScreenEvent(TravelEnterBookingDetailsEvent.OnDateRangeSelected(100L, 200L)))
        viewModel.onEvent(HotelEnterBookingDetailsEvent.ScreenEvent(TravelEnterBookingDetailsEvent.OnNextClick))
        
        val state = viewModel.uiState.value
        assertFalse(state.screenState.isDateErrorVisible)
        assertTrue(state.screenState.showGuestBottomSheet)
    }

    @Test
    fun `onAcceptClick shows error when adults count is less than 1`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(HotelEnterBookingDetailsEvent.ScreenEvent(TravelEnterBookingDetailsEvent.OnDateRangeSelected(100L, 200L)))
        viewModel.onEvent(HotelEnterBookingDetailsEvent.ScreenEvent(TravelEnterBookingDetailsEvent.OnNextClick))
        
        viewModel.onEvent(HotelEnterBookingDetailsEvent.OnAdultsCountChange(0))
        viewModel.onEvent(HotelEnterBookingDetailsEvent.ScreenEvent(TravelEnterBookingDetailsEvent.OnAcceptClick))
        
        val state = viewModel.uiState.value
        assertTrue(state.screenState.isGuestErrorVisible)
        assertTrue(state.screenState.showGuestBottomSheet)
    }

    @Test
    fun `onAcceptClick dismisses sheet and sets validation success on success`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(HotelEnterBookingDetailsEvent.ScreenEvent(TravelEnterBookingDetailsEvent.OnDateRangeSelected(100L, 200L)))
        viewModel.onEvent(HotelEnterBookingDetailsEvent.ScreenEvent(TravelEnterBookingDetailsEvent.OnNextClick))
        
        viewModel.onEvent(HotelEnterBookingDetailsEvent.OnAdultsCountChange(2))
        viewModel.onEvent(HotelEnterBookingDetailsEvent.ScreenEvent(TravelEnterBookingDetailsEvent.OnAcceptClick))
        
        val state = viewModel.uiState.value
        assertFalse(state.screenState.isGuestErrorVisible)
        assertFalse(state.screenState.showGuestBottomSheet)
        assertTrue(viewModel.validationSuccess.value)
    }

    @Test
    fun `resetValidationStatus sets validation success to false`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(HotelEnterBookingDetailsEvent.ScreenEvent(TravelEnterBookingDetailsEvent.OnDateRangeSelected(100L, 200L)))
        viewModel.onEvent(HotelEnterBookingDetailsEvent.ScreenEvent(TravelEnterBookingDetailsEvent.OnNextClick))
        viewModel.onEvent(HotelEnterBookingDetailsEvent.OnAdultsCountChange(2))
        viewModel.onEvent(HotelEnterBookingDetailsEvent.ScreenEvent(TravelEnterBookingDetailsEvent.OnAcceptClick))
        
        assertTrue(viewModel.validationSuccess.value)
        
        viewModel.resetValidationStatus()
        assertFalse(viewModel.validationSuccess.value)
    }
}
