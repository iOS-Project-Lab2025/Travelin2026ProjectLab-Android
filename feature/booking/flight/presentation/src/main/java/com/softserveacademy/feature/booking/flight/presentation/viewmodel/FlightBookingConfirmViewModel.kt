package com.softserveacademy.feature.booking.flight.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.BookingContactInfo
import com.softserveacademy.core.error.extension.onFailure
import com.softserveacademy.core.error.extension.onSuccess
import com.softserveacademy.feature.booking.common.domain.usecase.CreatePaymentIntentUseCase
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingDraftRepository
import com.softserveacademy.feature.booking.flight.presentation.events.FlightBookingConfirmEvent
import com.softserveacademy.feature.booking.flight.presentation.states.FlightBookingConfirmState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.model.FlightBooking
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingRepository
import java.util.UUID

@HiltViewModel
class FlightBookingConfirmViewModel @Inject constructor(
    private val draftRepository: FlightBookingDraftRepository,
    private val createPaymentIntentUseCase: CreatePaymentIntentUseCase,
    private val flightBookingRepository: FlightBookingRepository
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
                val totalPax = draft.passengerCounts.total
                val pricePerPax = draft.selectedOffers.values.sumOf { it.basePrice }

                _uiState.update { it.copy(
                    draft = draft,
                    totalPrice = (pricePerPax * totalPax).toInt(),
                    currency = "USD",
                    isLoading = false
                )}
            }
        }
    }

    fun onEvent(event: FlightBookingConfirmEvent) {
        when (event) {
            is FlightBookingConfirmEvent.OnConfirmClick -> {
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
            // Try real Stripe
            createPaymentIntentUseCase(state.totalPrice.toLong() * 100, "usd")
                .onSuccess { secret ->
                    _uiState.update { it.copy(clientSecret = secret, isPaymentSheetLoading = false) }
                }
                .onFailure { error ->
                    // FALLBACK: if server fails, we open simulator
                    _uiState.update { it.copy(
                        showPaymentSimulationSheet = true,
                        isPaymentSheetLoading = false
                    )}
                }
        }
    }

    /**
     * Finalizes the booking process after a successful payment cycle.
     * This is where the temporary Draft is converted into a formal Record.
     */
    private fun finalizeBooking() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val bookingRequest = createBookingFromDraft(BookingStatus.PENDING)

            if (bookingRequest != null) {
                flightBookingRepository.saveBooking(bookingRequest)
                    .onSuccess {

                        // clean draft
                        draftRepository.clearDraft()

                        // 2. Could save "officialBooking" in a state if SuccessScreen need it

                        _uiState.update { it.copy(isPaymentSuccessful = true, isLoading = false) }
                    }
                    .onFailure {
                        _uiState.update { it.copy(isLoading = false, error = "API Error: Could not issue tickets") }
                    }
            }
        }
    }

    /**
     * Internal logic to transform the temporary Draft into a final FlightBooking object.
     * This ensures data consistency before sending it to the Repository/API.
     */
    private fun createBookingFromDraft(status: BookingStatus): FlightBooking? {
        val state = _uiState.value
        val draft = state.draft ?: return null

        return FlightBooking(
            bookingId = UUID.randomUUID().toString(),
            userId = draft.userId ?: "", // User ID from auth
            confirmationCode = "FL-${System.currentTimeMillis() % 10000}",
            status = status,
            flights = draft.selectedOffers.values.map { it.flight },
            passengers = draft.passengers, // List of FlightPassengers
            tickets = emptyList(), // 0-N tickets generated by API
            contactInfo = draft.contactInfo ?: BookingContactInfo(), // Generic contact info
            totalAmount = state.totalPrice.toDouble(),
            currencyCode = state.currency,
            createdAt = System.currentTimeMillis()
        )
    }
}