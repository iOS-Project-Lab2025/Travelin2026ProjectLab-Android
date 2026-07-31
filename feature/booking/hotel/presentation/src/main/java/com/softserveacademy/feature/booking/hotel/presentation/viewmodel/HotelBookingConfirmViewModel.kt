package com.softserveacademy.feature.booking.hotel.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.BookingContactInfo
import com.softserveacademy.core.domain.model.BookingGuests
import com.softserveacademy.core.domain.model.BookingPrice
import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.model.HotelBooking
import com.softserveacademy.core.domain.repository.HotelBookingRepository
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
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HotelBookingConfirmViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val hotelBookingDraftRepository: HotelBookingDraftRepository,
    private val hotelRepo: HotelRepo,
    private val hotelBookingRepository: HotelBookingRepository,
    private val createPaymentIntentUseCase: CreatePaymentIntentUseCase
) : ViewModel() {

    private val hotelId: Int = checkNotNull(savedStateHandle["hotelId"])
    private var currentBookingId: String? = null

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
        val state = _uiState.value
        val amount = state.totalPrice.toLong()
        if (amount <= 0) {
            _uiState.update { it.copy(error = "Invalid price calculation") }
            return
        }
        _uiState.update { it.copy(isPaymentSheetLoading = true) }
        
        viewModelScope.launch {
            // Save initial PENDING booking
            val booking = createBookingFromState(BookingStatus.PENDING)
            if (booking != null) {
                currentBookingId = booking.bookingId
                hotelBookingRepository.saveBooking(booking)
            }

            createPaymentIntentUseCase(amount * 100, "usd") // Stripe expects amount in cents
                .onSuccess { secret ->
                    _uiState.update { it.copy(clientSecret = secret, isPaymentSheetLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to create payment intent",
                            isPaymentSheetLoading = false
                        )
                    }
                }
        }
    }

    private fun finalizeBooking() {
        viewModelScope.launch {
            currentBookingId?.let { id ->
                hotelBookingRepository.updateBookingStatus(id, BookingStatus.CONFIRMED)
            }
            hotelBookingDraftRepository.clearDraft(hotelId.toString())
            _uiState.update { it.copy(isPaymentSuccessful = true) }
        }
    }

    private fun createBookingFromState(status: BookingStatus): HotelBooking? {
        val state = _uiState.value
        val hotelDetails = state.hotelDetails ?: return null
        val room = state.selectedRoom ?: return null
        val draft = state.bookingDraft ?: return null

        return HotelBooking(
            bookingId = currentBookingId ?: UUID.randomUUID().toString(),
            hotelId = hotelDetails.id,
            roomId = room.id ?: 0,
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
