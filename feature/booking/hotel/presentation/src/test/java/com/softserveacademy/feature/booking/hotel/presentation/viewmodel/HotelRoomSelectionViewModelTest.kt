package com.softserveacademy.feature.booking.hotel.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.softserveacademy.core.domain.model.HotelRoom
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.feature.booking.common.domain.model.HotelBookingDraft
import com.softserveacademy.feature.booking.common.domain.repository.BookingRepository
import com.softserveacademy.feature.booking.hotel.presentation.events.HotelRoomSelectionEvent
import com.softserveacademy.feature.booking.hotel.presentation.states.RoomFilter
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HotelRoomSelectionViewModelTest {

    private lateinit var viewModel: HotelRoomSelectionViewModel
    private lateinit var hotelRepo: HotelRepo
    private lateinit var bookingRepository: BookingRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private val testDispatcher = StandardTestDispatcher()

    private val hotelId = 1
    private val mockRooms = listOf(
        HotelRoom(id = 1, type = "Room 1", description = "", maxOccupancy = "", bedType = "", bedCount = 1, amenities = emptyList(), pricePerNight = 100, isAvailable = true),
        HotelRoom(id = 2, type = "Room 2", description = "", maxOccupancy = "", bedType = "", bedCount = 2, amenities = emptyList(), pricePerNight = 200, isAvailable = false)
    )

    private val checkIn = 1000L
    private val checkOut = 2000L

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        hotelRepo = mockk()
        bookingRepository = mockk(relaxed = true)
        savedStateHandle = SavedStateHandle(mapOf("hotelId" to hotelId))

        coEvery { hotelRepo.getHotelRooms(hotelId, any(), any()) } returns mockRooms
        coEvery { bookingRepository.getHotelBookingDraft(hotelId.toString()) } returns HotelBookingDraft(
            hotelId = hotelId.toString(),
            checkInDate = checkIn,
            checkOutDate = checkOut
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads rooms and calculates night count`() = runTest {
        viewModel = HotelRoomSelectionViewModel(savedStateHandle, hotelRepo, bookingRepository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(mockRooms, state.rooms)
        assertFalse(state.isLoading)
        // (2000 - 1000) / (1000 * 60 * 60 * 24) is 0, coerceAtLeast(1) is 1
        assertEquals(1, state.nightCount)
    }

    @Test
    fun `applyFilters filters by availability`() = runTest {
        viewModel = HotelRoomSelectionViewModel(savedStateHandle, hotelRepo, bookingRepository)
        advanceUntilIdle()

        viewModel.onEvent(HotelRoomSelectionEvent.OnFilterSelected(RoomFilter.AVAILABLE))
        assertEquals(1, viewModel.uiState.value.filteredRooms.size)
        assertEquals(1, viewModel.uiState.value.filteredRooms[0].id)
    }

    @Test
    fun `applyFilters filters by bed count`() = runTest {
        viewModel = HotelRoomSelectionViewModel(savedStateHandle, hotelRepo, bookingRepository)
        advanceUntilIdle()

        viewModel.onEvent(HotelRoomSelectionEvent.OnFilterSelected(RoomFilter.TWO_BEDS))
        assertEquals(1, viewModel.uiState.value.filteredRooms.size)
        assertEquals(2, viewModel.uiState.value.filteredRooms[0].id)
    }

    @Test
    fun `onRoomSelected updates state`() = runTest {
        viewModel = HotelRoomSelectionViewModel(savedStateHandle, hotelRepo, bookingRepository)
        advanceUntilIdle()

        viewModel.onEvent(HotelRoomSelectionEvent.OnRoomSelected(1))
        assertEquals(1, viewModel.uiState.value.selectedRoomId)
    }

    @Test
    fun `onNextClick reserves room and updates draft`() = runTest {
        viewModel = HotelRoomSelectionViewModel(savedStateHandle, hotelRepo, bookingRepository)
        advanceUntilIdle()

        viewModel.onEvent(HotelRoomSelectionEvent.OnRoomSelected(1))
        
        coEvery { hotelRepo.reserveRoom(hotelId, 1, any(), any()) } returns Unit
        
        viewModel.onEvent(HotelRoomSelectionEvent.OnNextClick)
        advanceUntilIdle()

        coVerify { hotelRepo.reserveRoom(hotelId, 1, checkIn, checkOut) }
        coVerify { bookingRepository.saveHotelBookingDraft(any()) }
    }
}
