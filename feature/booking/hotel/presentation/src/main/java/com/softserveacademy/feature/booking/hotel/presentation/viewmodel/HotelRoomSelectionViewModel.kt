package com.softserveacademy.feature.booking.hotel.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.error.extension.onFailure
import com.softserveacademy.core.error.extension.onSuccess
import com.softserveacademy.feature.booking.hotel.domain.model.HotelBookingDraft
import com.softserveacademy.feature.booking.hotel.domain.usecase.GetHotelBookingDraftUseCase
import com.softserveacademy.core.domain.usecase.hotel.GetHotelRoomsUseCase
import com.softserveacademy.feature.booking.hotel.domain.usecase.SaveHotelBookingDraftUseCase
import com.softserveacademy.feature.booking.hotel.presentation.events.HotelRoomSelectionEvent
import com.softserveacademy.feature.booking.hotel.presentation.states.HotelRoomSelectionState
import com.softserveacademy.feature.booking.hotel.presentation.states.RoomFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
import javax.inject.Inject

/**
 * View model for the hotel room selection screen.
 *
 * @property savedStateHandle The handle to saved state.
 * @property getHotelRoomsUseCase Use case for fetching available hotel rooms.
 * @property getHotelBookingDraftUseCase Use case for retrieving booking drafts.
 * @property saveHotelBookingDraftUseCase Use case for saving booking drafts.
 */
@HiltViewModel
class HotelRoomSelectionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getHotelRoomsUseCase: GetHotelRoomsUseCase,
    private val getHotelBookingDraftUseCase: GetHotelBookingDraftUseCase,
    private val saveHotelBookingDraftUseCase: SaveHotelBookingDraftUseCase
) : ViewModel() {

    private val hotelId: String = checkNotNull(savedStateHandle["hotelId"])

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
            bookingDraft = getHotelBookingDraftUseCase(hotelId)
            val draft = bookingDraft
            val checkIn = draft?.checkIn ?: 0L
            val checkOut = draft?.checkOut ?: 0L
            val guestCount = (draft?.guests?.adults ?: 1) + (draft?.guests?.children ?: 0)

            // Calculate night count
            val nightCount = if (checkIn != 0L && checkOut != 0L) {
                ((checkOut - checkIn) / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(1)
            } else 1

            getHotelRoomsUseCase(hotelId, checkIn, checkOut, guestCount)
                .onSuccess { rooms ->
                    delay(1.seconds)
                    _uiState.update {
                        it.copy(
                            rooms = rooms,
                            nightCount = nightCount,
                            isLoading = false,
                            selectedRoomId = it.selectedRoomId ?: draft?.roomId
                        )
                    }
                    applyFilters()
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
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

    private fun saveRoomToDraft(roomId: String?) {
        viewModelScope.launch {
            val currentDraft = getHotelBookingDraftUseCase(hotelId)
                ?: HotelBookingDraft(hotelId = hotelId)
            val updatedDraft = currentDraft.copy(roomId = roomId)
            saveHotelBookingDraftUseCase(updatedDraft)
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

        val selectionReset = filtered.isEmpty() && currentState.selectedRoomId != null

        _uiState.update { 
            it.copy(
                filteredRooms = filtered,
                selectedRoomId = if (filtered.isEmpty()) null else it.selectedRoomId
            ) 
        }

        if (selectionReset) {
            savedStateHandle[KEY_SELECTED_ROOM_ID] = null
            saveRoomToDraft(null)
        }
    }

    private fun onNextClick() {
        val selectedRoomId = _uiState.value.selectedRoomId ?: return
        val draft = bookingDraft

        viewModelScope.launch {
            draft?.let {
                val updatedDraft = it.copy(roomId = selectedRoomId)
                saveHotelBookingDraftUseCase(updatedDraft)
            }
        }
    }

    companion object {
        private const val KEY_SELECTED_ROOM_ID = "selected_room_id"
        private const val KEY_SELECTED_FILTER = "selected_filter"
    }
}
