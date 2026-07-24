package com.softserveacademy.feature.booking.hotel.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.feature.booking.hotel.domain.model.HotelBookingDraft
import com.softserveacademy.feature.booking.hotel.domain.repository.HotelBookingDraftRepository
import com.softserveacademy.feature.booking.hotel.presentation.events.HotelRoomSelectionEvent
import com.softserveacademy.feature.booking.hotel.presentation.states.HotelRoomSelectionState
import com.softserveacademy.feature.booking.hotel.presentation.states.RoomFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model for the hotel room selection screen.
 *
 * @property savedStateHandle The handle to saved state.
 * @property hotelRepo Repository for fetching hotel and room data.
 * @property hotelBookingDraftRepository Repository for managing booking drafts.
 */
@HiltViewModel
class HotelRoomSelectionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val hotelRepo: HotelRepo,
    private val hotelBookingDraftRepository: HotelBookingDraftRepository
) : ViewModel() {

    private val hotelId: Int = checkNotNull(savedStateHandle["hotelId"])

    private val _uiState = MutableStateFlow(HotelRoomSelectionState(
        selectedRoomId = savedStateHandle[KEY_SELECTED_ROOM_ID],
        selectedFilter = savedStateHandle[KEY_SELECTED_FILTER] ?: RoomFilter.AVAILABLE
    ))

    /**
     * The current state of the hotel room selection screen.
     */
    val uiState: StateFlow<HotelRoomSelectionState> = _uiState.asStateFlow()

    private var bookingDraft: HotelBookingDraft? = null

    init {
        loadRooms()
    }

    private fun loadRooms() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            bookingDraft = hotelBookingDraftRepository.getDraft(hotelId.toString())
            val draft = bookingDraft
            val checkIn = draft?.checkIn ?: 0L
            val checkOut = draft?.checkOut ?: 0L

            val rooms = hotelRepo.getHotelRooms(hotelId, checkIn, checkOut)
            
            // Calculate night count
            val nightCount = if (checkIn != 0L && checkOut != 0L) {
                ((checkOut - checkIn) / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(1)
            } else 1

            _uiState.update {
                it.copy(
                    rooms = rooms,
                    nightCount = nightCount,
                    isLoading = false,
                    selectedRoomId = it.selectedRoomId ?: draft?.roomId?.toIntOrNull()
                )
            }
            applyFilters()
        }
    }

    /**
     * Handles UI events from the hotel room selection screen.
     *
     * @param event The event to handle.
     */
    fun onEvent(event: HotelRoomSelectionEvent) {
        when (event) {
            is HotelRoomSelectionEvent.OnFilterSelected -> {
                _uiState.update { it.copy(selectedFilter = event.filter) }
                savedStateHandle[KEY_SELECTED_FILTER] = event.filter
                applyFilters()
            }
            is HotelRoomSelectionEvent.OnRoomSelected -> {
                _uiState.update { it.copy(selectedRoomId = event.roomId) }
                savedStateHandle[KEY_SELECTED_ROOM_ID] = event.roomId
                saveRoomToDraft(event.roomId)
            }
            HotelRoomSelectionEvent.OnNextClick -> onNextClick()
            HotelRoomSelectionEvent.OnBackClick -> { /* Handled by navigation */ }
        }
    }

    private fun saveRoomToDraft(roomId: Int) {
        viewModelScope.launch {
            val currentDraft = hotelBookingDraftRepository.getDraft(hotelId.toString())
                ?: HotelBookingDraft(hotelId = hotelId.toString())
            val updatedDraft = currentDraft.copy(roomId = roomId.toString())
            hotelBookingDraftRepository.saveDraft(updatedDraft)
            bookingDraft = updatedDraft
        }
    }

    private fun applyFilters() {
        val currentState = _uiState.value
        val filtered = when (currentState.selectedFilter) {
            RoomFilter.AVAILABLE -> currentState.rooms.filter { it.isAvailable }
            RoomFilter.ALL -> currentState.rooms
            RoomFilter.ONE_BED -> currentState.rooms.filter { it.bedCount == 1 }
            RoomFilter.TWO_BEDS -> currentState.rooms.filter { it.bedCount == 2 }
        }
        _uiState.update { it.copy(filteredRooms = filtered) }
    }

    private fun onNextClick() {
        val selectedRoomId = _uiState.value.selectedRoomId ?: return
        val draft = bookingDraft
        val checkIn = draft?.checkIn ?: 0L
        val checkOut = draft?.checkOut ?: 0L

        viewModelScope.launch {
            // Real-time reservation considering dates
            hotelRepo.reserveRoom(hotelId, selectedRoomId, checkIn, checkOut)
            
            // Update booking draft
            draft?.let { 
                val updatedDraft = it.copy(roomId = selectedRoomId.toString())
                hotelBookingDraftRepository.saveDraft(updatedDraft)
            }
        }
    }

    companion object {
        private const val KEY_SELECTED_ROOM_ID = "selected_room_id"
        private const val KEY_SELECTED_FILTER = "selected_filter"
    }
}
