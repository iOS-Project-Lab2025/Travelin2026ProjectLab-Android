package com.softserveacademy.feature.booking.hotel.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.feature.booking.common.domain.repository.BookingRepository
import com.softserveacademy.feature.booking.common.domain.model.HotelBookingDraft
import com.softserveacademy.feature.booking.common.domain.usecase.ValidateEnterBookingDetailsUseCase
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent
import com.softserveacademy.feature.booking.common.presentation.states.TravelEnterBookingDetailsState
import com.softserveacademy.feature.booking.common.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

/**
 * View model for the enter booking details screen of the hotel booking flow.
 *
 * @property savedStateHandle The handle to saved state.
 * @property validateEnterBookingDetailsUseCase Use case for validating booking details input.
 * @property bookingRepository Repository for persisting booking drafts.
 */
@HiltViewModel
class HotelEnterBookingDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val validateEnterBookingDetailsUseCase: ValidateEnterBookingDetailsUseCase,
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TravelEnterBookingDetailsState())

    /**
     * The current state of the enter hotel booking details screen.
     */
    val uiState: StateFlow<TravelEnterBookingDetailsState> = _uiState.asStateFlow()
    
    private val _validationSuccess = MutableStateFlow(false)
    val validationSuccess: StateFlow<Boolean> = _validationSuccess.asStateFlow()

    /**
     * Resets the validation success flag.
     * Should be called when navigation to the next screen is handled or when navigating back.
     */
    fun resetValidationStatus() {
        _validationSuccess.value = false
    }

    private var hotelBookingDraft = HotelBookingDraft()

    init {
        val restoredDraft = savedStateHandle.get<HotelBookingDraft>(KEY_BOOKING_DRAFT)
        if (restoredDraft != null) {
            hotelBookingDraft = restoredDraft
            updateUiState()
        } else {
            val hotelIdFromSavedState = savedStateHandle.get<Int>("hotelId") ?: savedStateHandle.get<String>("hotelId")?.toIntOrNull()
            _uiState.update { it.copy(isLoading = true) }
            viewModelScope.launch {
                val repositoryDraft = hotelIdFromSavedState?.let { bookingRepository.getHotelBookingDraft(it.toString()) }
                
                hotelBookingDraft = repositoryDraft ?: HotelBookingDraft(hotelId = hotelIdFromSavedState?.toString())
                syncSavedState()
                updateUiState()
                delay(500.milliseconds) // Small delay for smooth transition
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Handles UI events from the enter hotel booking details screen.
     *
     * @param event The event to handle.
     */
    fun onEvent(event: TravelEnterBookingDetailsEvent) {
        when (event) {
            is TravelEnterBookingDetailsEvent.OnDateRangeSelected -> onDateRangeSelected(
                event.startDateMillis,
                event.endDateMillis
            )

            is TravelEnterBookingDetailsEvent.OnAdultsCountChange -> onAdultsCountChange(event.count)
            is TravelEnterBookingDetailsEvent.OnChildrenCountChange -> onChildrenCountChange(event.count)
            is TravelEnterBookingDetailsEvent.OnHasPetsChange -> onHasPetsChange(event.hasPets)
            TravelEnterBookingDetailsEvent.OnNextClick -> onNextClick()
            TravelEnterBookingDetailsEvent.OnBackClick -> { /* Handled by navigation */ }
            TravelEnterBookingDetailsEvent.OnDismissBottomSheet -> onDismissBottomSheet()
            TravelEnterBookingDetailsEvent.OnAcceptClick -> onAcceptClick()
        }
    }

    private fun onDateRangeSelected(startDateMillis: Long?, endDateMillis: Long?) {
        if (hotelBookingDraft.checkInDate == startDateMillis && hotelBookingDraft.checkOutDate == endDateMillis) return

        hotelBookingDraft = hotelBookingDraft.copy(
            checkInDate = startDateMillis,
            checkOutDate = endDateMillis
        )
        _uiState.update {
            it.copy(
                startDateMillis = startDateMillis,
                endDateMillis = endDateMillis,
                isDateErrorVisible = false
            )
        }
        syncSavedState()
    }

    private fun onAdultsCountChange(count: Int) {
        hotelBookingDraft = hotelBookingDraft.copy(amountOfAdults = count)
        _uiState.update {
            it.copy(
                adultsCount = count,
                isGuestErrorVisible = false
            )
        }
        syncSavedState()
    }

    private fun onChildrenCountChange(count: Int) {
        hotelBookingDraft = hotelBookingDraft.copy(amountOfChildren = count)
        _uiState.update {
            it.copy(childrenCount = count)
        }
        syncSavedState()
    }

    private fun onHasPetsChange(hasPets: Boolean) {
        hotelBookingDraft = hotelBookingDraft.copy(hasPets = hasPets)
        _uiState.update {
            it.copy(hasPets = hasPets)
        }
        syncSavedState()
    }

    private fun onNextClick() {
        val validationResult = validateEnterBookingDetailsUseCase.validateDates(
            hotelBookingDraft.checkInDate,
            hotelBookingDraft.checkOutDate
        )

        when (validationResult) {
            is ValidateEnterBookingDetailsUseCase.ValidationResult.Success -> {
                _uiState.update { it.copy(showGuestBottomSheet = true) }
            }

            is ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid -> {
                _uiState.update {
                    it.copy(
                        isDateErrorVisible = true,
                        dateErrorMessage = when (validationResult.error) {
                            ValidateEnterBookingDetailsUseCase.ValidationError.EMPTY_DATES ->
                                R.string.booking_error_select_dates

                            else -> null
                        }
                    )
                }
            }
        }
    }

    private fun onDismissBottomSheet() {
        _uiState.update { it.copy(showGuestBottomSheet = false, isGuestErrorVisible = false) }
    }

    private fun onAcceptClick() {
        val validationResult = validateEnterBookingDetailsUseCase.validateGuests(hotelBookingDraft.amountOfAdults)

        when (validationResult) {
            is ValidateEnterBookingDetailsUseCase.ValidationResult.Success -> {
                // Validation passed for both dates and guests
                _uiState.update { it.copy(showGuestBottomSheet = false) }
                _validationSuccess.value = true
            }

            is ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid -> {
                _uiState.update {
                    it.copy(
                        isGuestErrorVisible = true,
                        guestErrorMessage = when (validationResult.error) {
                            ValidateEnterBookingDetailsUseCase.ValidationError.AT_LEAST_ONE_ADULT ->
                                R.string.booking_error_at_least_one_adult

                            else -> null
                        }
                    )
                }
            }
        }
    }

    private fun updateUiState() {
        _uiState.update {
            it.copy(
                startDateMillis = hotelBookingDraft.checkInDate,
                endDateMillis = hotelBookingDraft.checkOutDate,
                adultsCount = hotelBookingDraft.amountOfAdults,
                childrenCount = hotelBookingDraft.amountOfChildren,
                hasPets = hotelBookingDraft.hasPets
            )
        }
    }

    private fun syncSavedState() {
        savedStateHandle[KEY_BOOKING_DRAFT] = hotelBookingDraft
        viewModelScope.launch {
            bookingRepository.saveHotelBookingDraft(hotelBookingDraft)
        }
    }

    companion object {
        private const val KEY_BOOKING_DRAFT = "booking_draft"
    }
}
