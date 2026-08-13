package com.softserveacademy.feature.booking.hotel.presentation.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.BookingContactInfo
import com.softserveacademy.core.domain.model.BookingGuests
import com.softserveacademy.core.domain.model.BookingPrice
import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.model.HotelBooking
import com.softserveacademy.feature.booking.common.domain.usecase.CreatePaymentIntentUseCase
import com.softserveacademy.core.error.extension.onFailure
import com.softserveacademy.core.error.extension.onSuccess
import com.softserveacademy.feature.booking.hotel.domain.usecase.ClearHotelBookingDraftUseCase
import com.softserveacademy.feature.booking.hotel.domain.usecase.GetHotelBookingDraftUseCase
import com.softserveacademy.core.domain.usecase.hotel.GetHotelDetailsUseCase
import com.softserveacademy.core.domain.usecase.hotel.GetRemoteHotelBookingsUseCase
import com.softserveacademy.core.domain.usecase.hotel.ReserveRoomUseCase
import com.softserveacademy.core.domain.usecase.hotel.SaveHotelBookingUseCase
import com.softserveacademy.core.domain.usecase.hotel.UpdateHotelBookingStatusUseCase
import com.softserveacademy.core.error.handler.ErrorHandler
import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.ErrorAction
import com.softserveacademy.core.error.model.UiText
import com.softserveacademy.feature.booking.hotel.presentation.events.HotelBookingConfirmEvent
import com.softserveacademy.feature.booking.hotel.presentation.states.HotelBookingConfirmState
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
class HotelBookingConfirmViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getHotelBookingDraftUseCase: GetHotelBookingDraftUseCase,
    private val clearHotelBookingDraftUseCase: ClearHotelBookingDraftUseCase,
    private val getHotelDetailsUseCase: GetHotelDetailsUseCase,
    private val reserveRoomUseCase: ReserveRoomUseCase,
    private val saveHotelBookingUseCase: SaveHotelBookingUseCase,
    private val updateHotelBookingStatusUseCase: UpdateHotelBookingStatusUseCase,
    private val createPaymentIntentUseCase: CreatePaymentIntentUseCase,
    private val getRemoteHotelBookingsUseCase: GetRemoteHotelBookingsUseCase,
    private val errorHandler: ErrorHandler,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val hotelId: String = checkNotNull(savedStateHandle["hotelId"])
    private var currentBookingId: String? = null

    private val _uiState = MutableStateFlow(HotelBookingConfirmState())
    val uiState: StateFlow<HotelBookingConfirmState> = _uiState.asStateFlow()

    init {
        loadBookingDetails()
        testRemoteBookingsFetch()
    }

    private fun testRemoteBookingsFetch() {
        viewModelScope.launch {
            getRemoteHotelBookingsUseCase()
                .onSuccess { bookings ->
                    android.util.Log.d("HotelBookingTest", "Successfully fetched ${bookings.size} remote bookings")
                    bookings.forEach { booking ->
                        android.util.Log.d("HotelBookingTest", "Booking: ID=${booking.bookingId}, User=${booking.userId}, Hotel=${booking.hotelId}")
                    }
                }
                .onFailure { error ->
                    android.util.Log.e("HotelBookingTest", "Failed to fetch remote bookings: $error")
                }
        }
    }

    private fun loadBookingDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val draft = getHotelBookingDraftUseCase(hotelId)
            if (draft != null) {
                getHotelDetailsUseCase(hotelId)
                    .onSuccess { hotelDetails ->
                        val selectedRoom = hotelDetails.rooms.find { it.id == draft.roomId }

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
                                hotel = hotelDetails,
                                selectedRoom = selectedRoom,
                                totalPrice = totalPrice,
                                error = null
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
                        } else "Failed to load details"
                        _uiState.update { it.copy(isLoading = false, error = message) }
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
            HotelBookingConfirmEvent.OnSimulateSuccessClick -> {
                _uiState.update { it.copy(showPaymentSimulationSheet = false, paymentSimulationError = null) }
                finalizeBooking()
            }
            HotelBookingConfirmEvent.OnSimulateFailureClick -> {
                _uiState.update { it.copy(paymentSimulationError = "Payment failed try again") }
            }
            HotelBookingConfirmEvent.OnDismissPaymentSimulationSheet -> {
                _uiState.update { it.copy(showPaymentSimulationSheet = false, paymentSimulationError = null) }
            }
            HotelBookingConfirmEvent.OnRetryClick -> {
                if (_uiState.value.hotel == null) {
                    loadBookingDetails()
                } else if (_uiState.value.error != null) {
                    // If we have hotel but still error, it might be payment intent error
                    _uiState.update { it.copy(error = null) }
                    createPaymentIntent()
                }
            }
            HotelBookingConfirmEvent.OnDismissError -> {
                _uiState.update { it.copy(error = null) }
            }
        }
    }

    private fun createPaymentIntent() {
        val state = _uiState.value
        val amount = state.totalPrice.toLong()
        if (amount <= 0) {
            _uiState.update { it.copy(error = "Invalid price calculation") }
            return
        }
        _uiState.update { it.copy(isPaymentSheetLoading = true) }

        viewModelScope.launch {
            // Save initial CREATED booking
            val booking = createBookingFromState(BookingStatus.CREATED)
            if (booking != null) {
                currentBookingId = booking.bookingId
                saveHotelBookingUseCase(booking)
                    .onFailure { _ ->
                        _uiState.update { it.copy(error = "Failed to save booking", isPaymentSheetLoading = false) }
                        updateHotelBookingStatusUseCase(booking.bookingId, BookingStatus.CANCELLED)
                        return@launch
                    }
            } else {
                _uiState.update { it.copy(error = "Failed to create booking data", isPaymentSheetLoading = false) }
                return@launch
            }

            createPaymentIntentUseCase(amount * 100, "usd") // Stripe expects amount in cents
                .onSuccess { secret ->
                    _uiState.update { it.copy(clientSecret = secret, isPaymentSheetLoading = false) }
                    // Update status to PENDING as we are now awaiting payment
                    updateHotelBookingStatusUseCase(booking.bookingId, BookingStatus.PENDING)
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
                    updateHotelBookingStatusUseCase(booking.bookingId, BookingStatus.CANCELLED)
                }
        }
    }

    private fun finalizeBooking() {
        val state = _uiState.value
        val hotelId = state.hotel?.id
        val roomId = state.selectedRoom?.id
        val checkIn = state.bookingDraft?.checkIn ?: 0L
        val checkOut = state.bookingDraft?.checkOut ?: 0L

        viewModelScope.launch {
            if (hotelId != null && roomId != null) {
                reserveRoomUseCase(hotelId, roomId, checkIn, checkOut)
            }

            currentBookingId?.let { id ->
                updateHotelBookingStatusUseCase(id, BookingStatus.COMPLETED)
            }
            clearHotelBookingDraftUseCase(hotelId.toString())
            _uiState.update { it.copy(isPaymentSuccessful = true) }
        }
    }

    @SuppressLint("HardwareIds")
    private fun createBookingFromState(status: BookingStatus): HotelBooking? {
        val state = _uiState.value
        val hotelDetails = state.hotel ?: return null
        val room = state.selectedRoom ?: return null
        val draft = state.bookingDraft ?: return null

        val deviceId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "unknown_device"

        return HotelBooking(
            bookingId = currentBookingId ?: UUID.randomUUID().toString(),
            userId = deviceId,
            //userId = "testing",
            hotelId = hotelDetails.id,
            roomId = room.id ?: "",
            checkIn = draft.checkIn ?: 0L,
            checkOut = draft.checkOut ?: 0L,
            guests = BookingGuests(
                adults = draft.guests.adults,
                children = draft.guests.children,
                pets = draft.guests.pets
            ),
            price = BookingPrice(
                roomPricePerNight = room.pricePerNight,
                roomPrice = state.totalPrice,
                taxes = 0,
                fees = 0,
                total = state.totalPrice
            ),
            confirmationCode = "HB-${System.currentTimeMillis() % 10000}",
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
