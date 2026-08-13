package com.softserveacademy.feature.booking.flight.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.error.extension.onFailure
import com.softserveacademy.core.error.extension.onSuccess
import com.softserveacademy.feature.booking.common.domain.usecase.CreatePaymentIntentUseCase
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingDraftRepository
import com.softserveacademy.feature.booking.flight.presentation.events.FlightBookingConfirmEvent
import com.softserveacademy.feature.booking.flight.presentation.states.FlightBookingConfirmState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class FlightBookingConfirmViewModel @Inject constructor(
    private val draftRepository: FlightBookingDraftRepository,
    private val createPaymentIntentUseCase: CreatePaymentIntentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlightBookingConfirmState())
    val uiState: StateFlow<FlightBookingConfirmState> = _uiState.asStateFlow()

    init {
        loadDraftAndCalculatePrice()
    }

    private fun loadDraftAndCalculatePrice() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            draftRepository.getDraft().filterNotNull().first().let { draft ->
                val totalPax = draft.adults + draft.children + draft.infants
                val pricePerPax = draft.selectedOffers.values.sumOf { it.basePrice }

                _uiState.update { it.copy(
                    draft = draft,
                    totalPrice = (pricePerPax * totalPax).toInt(),
                    // CORRECCIÓN: Usar USD por defecto o el currency del draft si existe
                    currency = "USD",
                    isLoading = false
                )}
            }
        }
    }

    fun onEvent(event: FlightBookingConfirmEvent) {
        when (event) {
            is FlightBookingConfirmEvent.OnConfirmClick -> {
                Log.d("FlightCheckout", "Intentando pagar monto: ${_uiState.value.totalPrice}")
                createPaymentIntent()
            }
            is FlightBookingConfirmEvent.OnPaymentSuccess -> finalizeBooking()
            is FlightBookingConfirmEvent.OnPaymentReset -> _uiState.update { it.copy(clientSecret = null, isPaymentSheetLoading = false) }
            is FlightBookingConfirmEvent.OnSimulateSuccessClick -> {
                _uiState.update { it.copy(showPaymentSimulationSheet = false) }
                finalizeBooking()
            }
            is FlightBookingConfirmEvent.OnSimulateFailureClick -> _uiState.update { it.copy(paymentSimulationError = "Simulated payment failed.") }
            is FlightBookingConfirmEvent.OnDismissPaymentSimulationSheet -> _uiState.update { it.copy(showPaymentSimulationSheet = false, paymentSimulationError = null) }
            is FlightBookingConfirmEvent.OnRetryClick -> loadDraftAndCalculatePrice()
            is FlightBookingConfirmEvent.OnDismissError -> _uiState.update { it.copy(error = null) }
        }
    }

    private fun createPaymentIntent() {
        val state = _uiState.value
        if (state.totalPrice <= 0) return

        _uiState.update { it.copy(isPaymentSheetLoading = true) }

        viewModelScope.launch {
            // Intentar Stripe real
            createPaymentIntentUseCase(state.totalPrice.toLong() * 100, "usd")
                .onSuccess { secret ->
                    _uiState.update { it.copy(clientSecret = secret, isPaymentSheetLoading = false) }
                }
                .onFailure { error ->
                    Log.w("FlightCheckout", "Stripe no disponible, activando simulador. Error: $error")
                    // FALLBACK: Si falla el servidor real, abrimos el simulador
                    _uiState.update { it.copy(
                        showPaymentSimulationSheet = true,
                        isPaymentSheetLoading = false
                    )}
                }
        }
    }

    private fun finalizeBooking() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1500) // Simulación de persistencia
            _uiState.update { it.copy(isPaymentSuccessful = true, isLoading = false) }
        }
    }
}