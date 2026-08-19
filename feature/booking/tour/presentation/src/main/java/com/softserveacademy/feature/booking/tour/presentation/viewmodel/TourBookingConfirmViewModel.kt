package com.softserveacademy.feature.booking.tour.presentation.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.BookingContactInfo
import com.softserveacademy.core.domain.model.BookingParticipants
import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.domain.model.TourBooking
import com.softserveacademy.core.domain.model.TourBookingPrice
import com.softserveacademy.core.domain.usecase.tour.GetTourDetailsUseCase
import com.softserveacademy.core.domain.usecase.tour.SaveTourBookingUseCase
import com.softserveacademy.core.domain.usecase.tour.UpdateTourBookingStatusUseCase
import com.softserveacademy.core.error.extension.onFailure
import com.softserveacademy.core.error.extension.onSuccess
import com.softserveacademy.core.error.handler.ErrorHandler
import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.ErrorAction
import com.softserveacademy.core.error.model.UiText
import com.softserveacademy.feature.booking.common.domain.usecase.CreatePaymentIntentUseCase
import com.softserveacademy.feature.booking.tour.domain.model.TourBookingDraft
import com.softserveacademy.feature.booking.tour.domain.usecase.ClearTourBookingDraftUseCase
import com.softserveacademy.feature.booking.tour.domain.usecase.GetTourBookingDraftUseCase
import com.softserveacademy.feature.booking.tour.presentation.events.TourBookingConfirmEvent
import com.softserveacademy.feature.booking.tour.presentation.states.TourBookingConfirmState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TourBookingConfirmViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getTourBookingDraftUseCase: GetTourBookingDraftUseCase,
    private val clearTourBookingDraftUseCase: ClearTourBookingDraftUseCase,
    private val getTourDetailsUseCase: GetTourDetailsUseCase,
    private val saveTourBookingUseCase: SaveTourBookingUseCase,
    private val updateTourBookingStatusUseCase: UpdateTourBookingStatusUseCase,
    private val createPaymentIntentUseCase: CreatePaymentIntentUseCase,
    private val errorHandler: ErrorHandler,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val tourId: String = checkNotNull(savedStateHandle["tourId"])
    private var currentBookingId: String? = null

    private val _uiState = MutableStateFlow(TourBookingConfirmState())
    val uiState: StateFlow<TourBookingConfirmState> = _uiState.asStateFlow()

    init {
        loadBookingDetails()
    }

    private fun loadBookingDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val draft = getTourBookingDraftUseCase(tourId)

            if (draft != null) {
                getTourDetailsUseCase(tourId)
                    .onSuccess { tourDetails ->
                        val totalPrice = calculateTotalPrice(tourDetails, draft)
                        _uiState.update {
                            it.copy(
                                tour = tourDetails,
                                bookingDraft = draft,
                                totalPrice = totalPrice,
                                isLoading = false
                            )
                    }
                }
                .onFailure { error ->
                    val action = errorHandler.handle(error)
                    val message = if (action is ErrorAction.ShowMessage) {
                        when (val uiText = action.message) {
                            is UiText.Raw -> uiText.value
                            is UiText.Resource -> "Failed to load booking details."
                        }
                    } else "Failed to load booking details"
                    _uiState.update { it.copy(isLoading = false, error = message) }
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load booking details") }
            }
        }
    }

    private fun calculateTotalPrice(tour: Tour, draft: TourBookingDraft): Double {
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
                cancelBooking()
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
        val state = _uiState.value
        val amount = (state.totalPrice * 100).toLong()
        if (amount <= 0) {
            _uiState.update { it.copy(error = "Invalid price calculation") }
            return
        }
        _uiState.update { it.copy(isPaymentSheetLoading = true) }

        viewModelScope.launch {
            val bookingId = currentBookingId
            if (bookingId == null) {
                // Save initial CREATED booking only if it doesn't exist yet
                val booking = createBookingFromState(BookingStatus.CREATED)
                if (booking != null) {
                    currentBookingId = booking.bookingId
                    saveTourBookingUseCase(booking)
                        .onFailure { _ ->
                            _uiState.update { it.copy(error = "Failed to save booking", isPaymentSheetLoading = false) }
                            updateTourBookingStatusUseCase(booking.bookingId, BookingStatus.CANCELLED)
                            return@launch
                        }
                } else {
                    _uiState.update { it.copy(error = "Failed to create booking data", isPaymentSheetLoading = false) }
                    return@launch
                }
            }

            val finalBookingId = currentBookingId ?: return@launch

            createPaymentIntentUseCase(amount, "usd")
                .onSuccess { secret ->
                    _uiState.update { it.copy(clientSecret = secret, isPaymentSheetLoading = false) }
                    // Update status to PENDING as we are now awaiting payment
                    updateTourBookingStatusUseCase(finalBookingId, BookingStatus.PENDING)
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

    private fun cancelBooking() {
        val bookingId = currentBookingId
        if (bookingId != null && !_uiState.value.isPaymentSuccessful) {
            viewModelScope.launch {
                updateTourBookingStatusUseCase(bookingId, BookingStatus.CANCELLED)
            }
        }
    }

    private fun finalizeBooking() {
        viewModelScope.launch {
            currentBookingId?.let { id ->
                updateTourBookingStatusUseCase(id, BookingStatus.COMPLETED)
            }
            clearTourBookingDraftUseCase(tourId)
            _uiState.update { it.copy(isPaymentSuccessful = true) }
        }
    }

    @SuppressLint("HardwareIds")
    private fun createBookingFromState(status: BookingStatus): TourBooking? {
        val state = _uiState.value
        val tourDetails = state.tour ?: return null
        val draft = state.bookingDraft ?: return null

        val deviceId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "unknown_device"

        return TourBooking(
            bookingId = currentBookingId ?: UUID.randomUUID().toString(),
            userId = deviceId,
            tourId = tourDetails.id,
            startDate = draft.startDate ?: 0L,
            endDate = draft.endDate ?: 0L,
            participants = BookingParticipants(
                adults = draft.participants.adults,
                children = draft.participants.children,
                infants = draft.participants.infants
            ),
            price = TourBookingPrice(
                ratePerAdult = tourDetails.rates.adults,
                ratePerChildren = tourDetails.rates.children,
                ratePerInfant = tourDetails.rates.infants,
                subtotal = state.totalPrice,
                taxes = 0.0,
                fees = 0.0,
                total = state.totalPrice,
                currencyCode = "USD"
            ),
            confirmationCode = "TB-${System.currentTimeMillis() % 10000}",
            status = status,
            createdAt = System.currentTimeMillis(),
            contactInfo = BookingContactInfo(
                firstName = draft.contactInfo.firstName,
                lastName = draft.contactInfo.lastName,
                email = draft.contactInfo.email,
                countryCode = draft.contactInfo.countryCode,
                phoneNumber = draft.contactInfo.phoneNumber
            )
        )
    }
}
