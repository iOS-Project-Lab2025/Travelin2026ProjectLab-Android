package com.softserveacademy.feature.booking.tour.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.repository.TourRepo
import com.softserveacademy.core.error.extension.onFailure
import com.softserveacademy.core.error.extension.onSuccess
import com.softserveacademy.core.error.handler.ErrorHandler
import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.core.error.model.ErrorAction
import com.softserveacademy.core.error.model.UiText
import com.softserveacademy.feature.booking.common.domain.usecase.CreatePaymentIntentUseCase
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
    private val tourRepo: TourRepo,
    private val createPaymentIntentUseCase: CreatePaymentIntentUseCase,
    private val errorHandler: ErrorHandler
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
                createPaymentIntent()
            }
            TourBookingConfirmEvent.OnBackClick -> {
                // Handled in UI
            }
            TourBookingConfirmEvent.OnRetryClick -> {
                if (_uiState.value.tour == null) {
                    loadBookingDetails()
                } else if (_uiState.value.error != null) {
                    _uiState.update { it.copy(error = null) }
                    createPaymentIntent()
                }
            }
            TourBookingConfirmEvent.OnDismissError -> _uiState.update { it.copy(error = null) }
            TourBookingConfirmEvent.OnPaymentSuccess -> finalizeBooking()
            TourBookingConfirmEvent.OnPaymentReset -> _uiState.update { it.copy(clientSecret = null, isPaymentSheetLoading = false) }
            TourBookingConfirmEvent.OnSimulateSuccessClick -> {
                _uiState.update { it.copy(showPaymentSimulationSheet = false, paymentSimulationError = null) }
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

    private fun createPaymentIntent() {
        val amount = (_uiState.value.totalPrice * 100).toLong()
        if (amount <= 0) {
            _uiState.update { it.copy(error = "Invalid price calculation") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isPaymentSheetLoading = true) }
            createPaymentIntentUseCase(amount, "usd")
                .onSuccess { secret ->
                    _uiState.update { it.copy(clientSecret = secret, isPaymentSheetLoading = false) }
                }
                .onFailure { error ->
                    if (error is AppError.Auth) {
                        _uiState.update {
                            it.copy(
                                showPaymentSimulationSheet = true,
                                isPaymentSheetLoading = false
                            )
                        }
                    } else {
                        val action = errorHandler.handle(error)
                        val message = if (action is ErrorAction.ShowMessage) {
                            when (val uiText = action.message) {
                                is UiText.Raw -> uiText.value
                                is UiText.Resource -> "An error occurred during payment initialization."
                            }
                        } else "Network error. Please try again."

                        _uiState.update {
                            it.copy(
                                error = message,
                                isPaymentSheetLoading = false
                            )
                        }
                    }
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
