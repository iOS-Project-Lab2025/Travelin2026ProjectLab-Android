package com.softserveacademy.feature.booking.hotel.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.HotelRoom
import com.softserveacademy.core.domain.usecase.hotel.GetHotelDetailsUseCase
import com.softserveacademy.core.domain.usecase.hotel.GetRemoteHotelBookingsUseCase
import com.softserveacademy.core.domain.usecase.hotel.ReserveRoomUseCase
import com.softserveacademy.core.domain.usecase.hotel.SaveHotelBookingUseCase
import com.softserveacademy.core.domain.usecase.hotel.UpdateHotelBookingStatusUseCase
import com.softserveacademy.core.error.handler.ErrorHandler
import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.core.error.model.ErrorAction
import com.softserveacademy.core.error.model.UiText
import com.softserveacademy.feature.booking.common.domain.usecase.CreatePaymentIntentUseCase
import com.softserveacademy.feature.booking.hotel.domain.model.HotelBookingDraft
import com.softserveacademy.feature.booking.hotel.domain.usecase.ClearHotelBookingDraftUseCase
import com.softserveacademy.feature.booking.hotel.domain.usecase.GetHotelBookingDraftUseCase
import com.softserveacademy.feature.booking.hotel.presentation.events.HotelBookingConfirmEvent
import io.mockk.coEvery
import io.mockk.every
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HotelBookingConfirmViewModelTest {

    private lateinit var viewModel: HotelBookingConfirmViewModel
    private val savedStateHandle = SavedStateHandle(mapOf("hotelId" to "hotel1"))
    private val getHotelBookingDraftUseCase: GetHotelBookingDraftUseCase = mockk()
    private val clearHotelBookingDraftUseCase: ClearHotelBookingDraftUseCase = mockk()
    private val getHotelDetailsUseCase: GetHotelDetailsUseCase = mockk()
    private val reserveRoomUseCase: ReserveRoomUseCase = mockk()
    private val saveHotelBookingUseCase: SaveHotelBookingUseCase = mockk()
    private val updateHotelBookingStatusUseCase: UpdateHotelBookingStatusUseCase = mockk()
    private val createPaymentIntentUseCase: CreatePaymentIntentUseCase = mockk()
    private val getRemoteHotelBookingsUseCase: GetRemoteHotelBookingsUseCase = mockk()
    private val errorHandler: ErrorHandler = mockk()
    private val context: Context = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        val draft = HotelBookingDraft(
            hotelId = "hotel1",
            roomId = "room1",
            checkIn = 1000L,
            checkOut = 2000L
        )
        val hotel = Hotel(
            id = "hotel1",
            name = "Test Hotel",
            description = "Desc",
            address = "Loc",
            imageList = emptyList(),
            rooms = listOf(HotelRoom(id = "room1", type = "Type", description = "", maxOccupancy = 2, bedType = "", bedCount = 1, amenities = emptyList(), pricePerNight = 100, isAvailable = true)),
            reviewRating = 4.5,
            numberOfReviews = 10
        )

        coEvery { getHotelBookingDraftUseCase("hotel1") } returns draft
        coEvery { getHotelDetailsUseCase("hotel1") } returns AppResult.Success(hotel)
        coEvery { saveHotelBookingUseCase(any()) } returns AppResult.Success(Unit)
        coEvery { updateHotelBookingStatusUseCase(any(), any()) } returns AppResult.Success(Unit)
        coEvery { getRemoteHotelBookingsUseCase() } returns AppResult.Success(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `createPaymentIntent shows simulation sheet on Auth error`() = runTest {
        coEvery { createPaymentIntentUseCase(any(), any()) } returns AppResult.Failure(AppError.Auth.Unauthorized)

        viewModel = HotelBookingConfirmViewModel(
            savedStateHandle,
            getHotelBookingDraftUseCase,
            clearHotelBookingDraftUseCase,
            getHotelDetailsUseCase,
            reserveRoomUseCase,
            saveHotelBookingUseCase,
            updateHotelBookingStatusUseCase,
            createPaymentIntentUseCase,
            getRemoteHotelBookingsUseCase,
            errorHandler,
            context
        )
        advanceUntilIdle()

        viewModel.onEvent(HotelBookingConfirmEvent.OnConfirmClick)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.showPaymentSimulationSheet)
        assertFalse(state.isPaymentSheetLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `createPaymentIntent shows error message on Network error`() = runTest {
        val networkError = AppError.Network.NoConnection
        coEvery { createPaymentIntentUseCase(any(), any()) } returns AppResult.Failure(networkError)
        every { errorHandler.handle(networkError) } returns ErrorAction.ShowMessage(UiText.Raw("No connection"))

        viewModel = HotelBookingConfirmViewModel(
            savedStateHandle,
            getHotelBookingDraftUseCase,
            clearHotelBookingDraftUseCase,
            getHotelDetailsUseCase,
            reserveRoomUseCase,
            saveHotelBookingUseCase,
            updateHotelBookingStatusUseCase,
            createPaymentIntentUseCase,
            getRemoteHotelBookingsUseCase,
            errorHandler,
            context
        )
        advanceUntilIdle()

        viewModel.onEvent(HotelBookingConfirmEvent.OnConfirmClick)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.showPaymentSimulationSheet)
        assertFalse(state.isPaymentSheetLoading)
        assertEquals("No connection", state.error)
    }

    @Test
    fun `loadBookingDetails handles error correctly`() = runTest {
        val networkError = AppError.Network.Timeout
        coEvery { getHotelDetailsUseCase(any()) } returns AppResult.Failure(networkError)
        every { errorHandler.handle(any()) } returns ErrorAction.ShowMessage(UiText.Raw("Timeout error"))

        viewModel = HotelBookingConfirmViewModel(
            savedStateHandle,
            getHotelBookingDraftUseCase,
            clearHotelBookingDraftUseCase,
            getHotelDetailsUseCase,
            reserveRoomUseCase,
            saveHotelBookingUseCase,
            updateHotelBookingStatusUseCase,
            createPaymentIntentUseCase,
            getRemoteHotelBookingsUseCase,
            errorHandler,
            context
        )
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Timeout error", state.error)
    }

    @Test
    fun `OnRetryClick triggers reload when hotel is null`() = runTest {
        coEvery { getHotelDetailsUseCase(any()) } returns AppResult.Failure(AppError.Unknown(Exception("Failed")))
        every { errorHandler.handle(any()) } returns ErrorAction.ShowMessage(UiText.Raw("Error"))

        viewModel = HotelBookingConfirmViewModel(
            savedStateHandle,
            getHotelBookingDraftUseCase,
            clearHotelBookingDraftUseCase,
            getHotelDetailsUseCase,
            reserveRoomUseCase,
            saveHotelBookingUseCase,
            updateHotelBookingStatusUseCase,
            createPaymentIntentUseCase,
            getRemoteHotelBookingsUseCase,
            errorHandler,
            context
        )
        advanceUntilIdle()

        coEvery { getHotelDetailsUseCase(any()) } returns AppResult.Success(mockk(relaxed = true))
        viewModel.onEvent(HotelBookingConfirmEvent.OnRetryClick)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.hotel != null)
        assertEquals(null, state.error)
    }
}
