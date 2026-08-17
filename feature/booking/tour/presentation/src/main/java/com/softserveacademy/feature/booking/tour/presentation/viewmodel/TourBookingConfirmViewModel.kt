package com.softserveacademy.feature.booking.tour.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.repository.TourRepo
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.feature.booking.tour.domain.usecase.ClearTourBookingDraftUseCase
import com.softserveacademy.feature.booking.tour.domain.usecase.GetTourBookingDraftUseCase
import com.softserveacademy.feature.booking.tour.presentation.events.TourBookingConfirmEvent
import com.softserveacademy.feature.booking.tour.presentation.states.TourBookingConfirmState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TourBookingConfirmViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getTourBookingDraftUseCase: GetTourBookingDraftUseCase,
    private val clearTourBookingDraftUseCase: ClearTourBookingDraftUseCase,
    private val tourRepo: TourRepo
) : ViewModel() {

    private val tourId: String = checkNotNull(savedStateHandle["tourId"])

    private val _uiState = MutableStateFlow(TourBookingConfirmState())
    val uiState: StateFlow<TourBookingConfirmState> = _uiState.asStateFlow()

    init {
        loadBookingDetails()
    }

    private fun loadBookingDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val draft = getTourBookingDraftUseCase(tourId)
            val tourResult = tourRepo.getTourById(tourId)

            if (tourResult is AppResult.Success && draft != null) {
                val tour = tourResult.data
                val totalPrice = calculateTotalPrice(tour, draft)
                _uiState.update { 
                    it.copy(
                        tour = tour,
                        bookingDraft = draft,
                        totalPrice = totalPrice,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load booking details") }
            }
        }
    }

    private fun calculateTotalPrice(tour: com.softserveacademy.core.domain.model.Tour, draft: com.softserveacademy.feature.booking.tour.domain.model.TourBookingDraft): Double {
        val adults = draft.participants.adults * tour.rates.adults
        val children = draft.participants.children * tour.rates.children
        val infants = draft.participants.infants * tour.rates.infants
        return adults + children + infants
    }

    fun onEvent(event: TourBookingConfirmEvent) {
        when (event) {
            TourBookingConfirmEvent.OnConfirmClick -> {
                _uiState.update { it.copy(showPaymentSimulationSheet = true) }
            }
            TourBookingConfirmEvent.OnBackClick -> {
                // Handled in UI
            }
            TourBookingConfirmEvent.OnRetryClick -> loadBookingDetails()
            TourBookingConfirmEvent.OnDismissError -> _uiState.update { it.copy(error = null) }
            TourBookingConfirmEvent.OnPaymentSuccess -> finalizeBooking()
            TourBookingConfirmEvent.OnPaymentReset -> _uiState.update { it.copy(clientSecret = null) }
            TourBookingConfirmEvent.OnSimulateSuccessClick -> {
                _uiState.update { it.copy(showPaymentSimulationSheet = false) }
                finalizeBooking()
            }
            TourBookingConfirmEvent.OnSimulateFailureClick -> {
                _uiState.update { it.copy(paymentSimulationError = "Payment failed simulation") }
            }
            TourBookingConfirmEvent.OnDismissPaymentSimulationSheet -> {
                _uiState.update { it.copy(showPaymentSimulationSheet = false, paymentSimulationError = null) }
            }
        }
    }

    private fun finalizeBooking() {
        viewModelScope.launch {
            clearTourBookingDraftUseCase(tourId)
            _uiState.update { it.copy(isPaymentSuccessful = true) }
        }
    }
}
