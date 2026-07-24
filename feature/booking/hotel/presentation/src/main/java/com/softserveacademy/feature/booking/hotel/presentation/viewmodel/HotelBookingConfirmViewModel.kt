package com.softserveacademy.feature.booking.hotel.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.feature.booking.hotel.domain.repository.HotelBookingDraftRepository
import com.softserveacademy.feature.booking.hotel.presentation.events.HotelBookingConfirmEvent
import com.softserveacademy.feature.booking.hotel.presentation.states.HotelBookingConfirmState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotelBookingConfirmViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val hotelBookingDraftRepository: HotelBookingDraftRepository,
    private val hotelRepo: HotelRepo
) : ViewModel() {

    private val hotelId: Int = checkNotNull(savedStateHandle["hotelId"])

    private val _uiState = MutableStateFlow(HotelBookingConfirmState())
    val uiState: StateFlow<HotelBookingConfirmState> = _uiState.asStateFlow()

    init {
        loadBookingDetails()
    }

    private fun loadBookingDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val draft = hotelBookingDraftRepository.getDraft(hotelId.toString())
                if (draft != null) {
                    val hotelDetails = hotelRepo.getHotelById(hotelId)
                    val selectedRoom = hotelDetails.rooms.find { it.id.toString() == draft.roomId }
                    
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            bookingDraft = draft,
                            hotelDetails = hotelDetails,
                            selectedRoom = selectedRoom
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "No booking draft found") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
            }
        }
    }

    fun onEvent(event: HotelBookingConfirmEvent) {
        when (event) {
            HotelBookingConfirmEvent.OnConfirmClick -> {
                // Handle booking confirmation (e.g., call repository to save booking)
            }
            HotelBookingConfirmEvent.OnBackClick -> { /* Handled by navigation */ }
        }
    }
}
