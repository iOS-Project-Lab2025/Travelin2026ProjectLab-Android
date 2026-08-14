package com.softserveacademy.feature.booking.hotel.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.softserveacademy.core.domain.model.HotelRoom
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.feature.booking.hotel.domain.model.HotelBookingDraft
import com.softserveacademy.feature.booking.hotel.domain.usecase.GetHotelBookingDraftUseCase
import com.softserveacademy.core.domain.usecase.hotel.GetFilterRoomsUseCase
import com.softserveacademy.core.domain.usecase.hotel.GetAvailableRoomsUseCase
import com.softserveacademy.feature.booking.hotel.domain.usecase.SaveHotelBookingDraftUseCase
import com.softserveacademy.feature.booking.hotel.presentation.events.HotelRoomSelectionEvent
import com.softserveacademy.feature.booking.hotel.presentation.states.RoomFilter
import com.softserveacademy.core.error.handler.ErrorHandler
import com.softserveacademy.core.error.model.ErrorAction
import com.softserveacademy.core.error.model.UiText
import io.mockk.coEvery
import io.mockk.coVerify
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
class HotelRoomSelectionViewModelTest {

    private lateinit var viewModel: HotelRoomSelectionViewModel
    private lateinit var getFilterRoomsUseCase: GetFilterRoomsUseCase
    private lateinit var getAvailableRoomsUseCase: GetAvailableRoomsUseCase
    private lateinit var getHotelBookingDraftUseCase: GetHotelBookingDraftUseCase
    private lateinit var saveHotelBookingDraftUseCase: SaveHotelBookingDraftUseCase
    private lateinit var errorHandler: ErrorHandler
    private lateinit var savedStateHandle: SavedStateHandle
    private val testDispatcher = StandardTestDispatcher()

    private val hotelId = "1"
    private val mockRooms = listOf(
        HotelRoom(id = "1", type = "Room 1", description = "", maxOccupancy = 2, bedType = "", bedCount = 1, amenities = emptyList(), pricePerNight = 100),
        HotelRoom(id = "2", type = "Room 2", description = "", maxOccupancy = 2, bedType = "", bedCount = 2, amenities = emptyList(), pricePerNight = 200)
    )

    private val checkIn = 1000L
    private val checkOut = 2000L

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getFilterRoomsUseCase = mockk()
        getAvailableRoomsUseCase = mockk()
        getHotelBookingDraftUseCase = mockk(relaxed = true)
        saveHotelBookingDraftUseCase = mockk(relaxed = true)
        errorHandler = mockk(relaxed = true)
        savedStateHandle = SavedStateHandle(mapOf("hotelId" to hotelId))

        coEvery { getFilterRoomsUseCase(hotelId, any(), any()) } returns AppResult.Success(mockRooms)
        coEvery { getAvailableRoomsUseCase(hotelId, any(), any(), any(), any()) } returns AppResult.Success(listOf(mockRooms[0]))
        coEvery { getHotelBookingDraftUseCase(hotelId) } returns HotelBookingDraft(
            hotelId = hotelId,
            checkIn = checkIn,
            checkOut = checkOut
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads rooms and calculates night count`() = runTest {
        viewModel = HotelRoomSelectionViewModel(
            savedStateHandle,
            getFilterRoomsUseCase,
            getAvailableRoomsUseCase,
            getHotelBookingDraftUseCase,
            saveHotelBookingDraftUseCase,
            errorHandler
        )
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(mockRooms, state.rooms)
        assertFalse(state.isLoading)
        assertEquals(1, state.nightCount)
        assertTrue(state.availableRoomIds.contains("1"))
    }

    @Test
    fun `applyFilters filters by availability`() = runTest {
        viewModel = HotelRoomSelectionViewModel(
            savedStateHandle,
            getFilterRoomsUseCase,
            getAvailableRoomsUseCase,
            getHotelBookingDraftUseCase,
            saveHotelBookingDraftUseCase,
            errorHandler
        )
        advanceUntilIdle()

        viewModel.onEvent(HotelRoomSelectionEvent.OnFilterSelected(RoomFilter.AVAILABLE))
        assertEquals(1, viewModel.uiState.value.filteredRooms.size)
        assertEquals("1", viewModel.uiState.value.filteredRooms[0].id)
    }

    @Test
    fun `applyFilters filters by bed count`() = runTest {
        viewModel = HotelRoomSelectionViewModel(
            savedStateHandle,
            getFilterRoomsUseCase,
            getAvailableRoomsUseCase,
            getHotelBookingDraftUseCase,
            saveHotelBookingDraftUseCase,
            errorHandler
        )
        advanceUntilIdle()

        viewModel.onEvent(HotelRoomSelectionEvent.OnFilterSelected(RoomFilter.TWO_BEDS))
        assertEquals(1, viewModel.uiState.value.filteredRooms.size)
        assertEquals("2", viewModel.uiState.value.filteredRooms[0].id)
    }

    @Test
    fun `onRoomSelected updates state`() = runTest {
        viewModel = HotelRoomSelectionViewModel(
            savedStateHandle,
            getFilterRoomsUseCase,
            getAvailableRoomsUseCase,
            getHotelBookingDraftUseCase,
            saveHotelBookingDraftUseCase,
            errorHandler
        )
        advanceUntilIdle()

        viewModel.onEvent(HotelRoomSelectionEvent.OnRoomSelected("1"))
        assertEquals("1", viewModel.uiState.value.selectedRoomId)
    }

    @Test
    fun `applyFilters resets selectedRoomId when filteredRooms is empty and persists it`() = runTest {
        val specialRooms = listOf(
            HotelRoom(id = "1", type = "Room 1", description = "", maxOccupancy = 2, bedType = "", bedCount = 2, amenities = emptyList(), pricePerNight = 100)
        )
        coEvery { getFilterRoomsUseCase(hotelId, any(), any()) } returns AppResult.Success(specialRooms)
        coEvery { getAvailableRoomsUseCase(any(), any(), any(), any(), any()) } returns AppResult.Success(specialRooms)
        
        viewModel = HotelRoomSelectionViewModel(
            savedStateHandle,
            getFilterRoomsUseCase,
            getAvailableRoomsUseCase,
            getHotelBookingDraftUseCase,
            saveHotelBookingDraftUseCase,
            errorHandler
        )
        advanceUntilIdle()

        viewModel.onEvent(HotelRoomSelectionEvent.OnRoomSelected("1"))
        advanceUntilIdle()
        assertEquals("1", viewModel.uiState.value.selectedRoomId)

        viewModel.onEvent(HotelRoomSelectionEvent.OnFilterSelected(RoomFilter.ONE_BED))
        advanceUntilIdle()
        
        assertEquals(0, viewModel.uiState.value.filteredRooms.size)
        assertEquals(null, viewModel.uiState.value.selectedRoomId)
        
        coVerify { saveHotelBookingDraftUseCase(match { it.roomId == null }) }
    }

    @Test
    fun `onNextClick saves draft`() = runTest {
        viewModel = HotelRoomSelectionViewModel(
            savedStateHandle,
            getFilterRoomsUseCase,
            getAvailableRoomsUseCase,
            getHotelBookingDraftUseCase,
            saveHotelBookingDraftUseCase,
            errorHandler
        )
        advanceUntilIdle()

        viewModel.onEvent(HotelRoomSelectionEvent.OnRoomSelected("1"))

        viewModel.onEvent(HotelRoomSelectionEvent.OnNextClick)
        advanceUntilIdle()

        coVerify { saveHotelBookingDraftUseCase(match { it.roomId == "1" }) }
    }

    @Test
    fun `loadRooms handles failure correctly`() = runTest {
        coEvery { getFilterRoomsUseCase(any(), any(), any()) } returns AppResult.Failure(mockk())
        every { errorHandler.handle(any()) } returns ErrorAction.ShowMessage(UiText.Raw("Room error"))

        viewModel = HotelRoomSelectionViewModel(
            savedStateHandle,
            getFilterRoomsUseCase,
            getAvailableRoomsUseCase,
            getHotelBookingDraftUseCase,
            saveHotelBookingDraftUseCase,
            errorHandler
        )
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Room error", state.error)
        assertFalse(state.isLoading)
    }

    @Test
    fun `OnRetryClick reloads rooms`() = runTest {
        coEvery { getFilterRoomsUseCase(any(), any(), any()) } returns AppResult.Failure(mockk())
        every { errorHandler.handle(any()) } returns ErrorAction.ShowMessage(UiText.Raw("Error"))

        viewModel = HotelRoomSelectionViewModel(
            savedStateHandle,
            getFilterRoomsUseCase,
            getAvailableRoomsUseCase,
            getHotelBookingDraftUseCase,
            saveHotelBookingDraftUseCase,
            errorHandler
        )
        advanceUntilIdle()

        coEvery { getFilterRoomsUseCase(any(), any(), any()) } returns AppResult.Success(mockRooms)
        coEvery { getAvailableRoomsUseCase(any(), any(), any(), any(), any()) } returns AppResult.Success(mockRooms)
        viewModel.onEvent(HotelRoomSelectionEvent.OnRetryClick)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(mockRooms, state.rooms)
        assertEquals(null, state.error)
    }
}
