package com.softserveacademy.feature.booking.hotel.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.feature.booking.common.domain.usecase.CreatePaymentIntentUseCase
import com.softserveacademy.core.error.extension.onFailure
import com.softserveacademy.core.error.extension.onSuccess
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
    private val hotelRepo: HotelRepo,
    private val createPaymentIntentUseCase: CreatePaymentIntentUseCase
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
            val draft = hotelBookingDraftRepository.getDraft(hotelId.toString())
            if (draft != null) {
                hotelRepo.getHotelById(hotelId)
                    .onSuccess { hotelDetails ->
                        val selectedRoom = hotelDetails.rooms.find { it.id.toString() == draft.roomId }

                        val checkIn = draft.checkIn
                        val checkOut = draft.checkOut
                        val nights = if (checkIn != null && checkOut != null) {
                            ((checkOut - checkIn) / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(1)
                        } else 1
                        val totalPrice = (selectedRoom?.pricePerNight ?: 0) * nights

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                bookingDraft = draft,
                                hotelDetails = hotelDetails,
                                selectedRoom = selectedRoom,
                                totalPrice = totalPrice
                            )
                        }
                    }
                    .onFailure { e ->
                        _uiState.update { it.copy(isLoading = false, error = e.toString()) }
                    }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "No booking draft found") }
            }
        }
    }

    fun onEvent(event: HotelBookingConfirmEvent) {
        when (event) {
            HotelBookingConfirmEvent.OnConfirmClick -> {
                createPaymentIntent()
            }
            HotelBookingConfirmEvent.OnBackClick -> { /* Handled by navigation */ }
            HotelBookingConfirmEvent.OnPaymentSuccess -> {
                // Finalize booking after successful payment
                finalizeBooking()
            }
            HotelBookingConfirmEvent.OnPaymentReset -> {
                _uiState.update { it.copy(clientSecret = null) }
            }
        }
    }

    private fun createPaymentIntent() {
        val amount = _uiState.value.totalPrice.toLong()
        if (amount <= 0) {
            _uiState.update { it.copy(error = "Invalid price calculation") }
            return
        }
        viewModelScope.launch {
            createPaymentIntentUseCase(amount * 100, "usd") // Stripe expects amount in cents
                .onSuccess { secret ->
                    _uiState.update { it.copy(clientSecret = secret) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message ?: "Failed to create payment intent") }
                }
        }
    }

    private fun finalizeBooking() {
        // Implement final booking logic here
    }
}
