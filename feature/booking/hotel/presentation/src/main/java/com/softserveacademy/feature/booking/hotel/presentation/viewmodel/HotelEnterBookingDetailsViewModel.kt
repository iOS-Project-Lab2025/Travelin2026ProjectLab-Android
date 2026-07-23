package com.softserveacademy.feature.booking.hotel.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.feature.booking.hotel.domain.repository.HotelBookingDraftRepository
import com.softserveacademy.feature.booking.hotel.domain.model.HotelBookingDraft
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
 * @property hotelBookingDraftRepository Repository for persisting booking drafts.
 */
@HiltViewModel
class HotelEnterBookingDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val validateEnterBookingDetailsUseCase: ValidateEnterBookingDetailsUseCase,
    private val hotelBookingDraftRepository: HotelBookingDraftRepository
) : ViewModel() {

    private val hotelId: Int = checkNotNull(savedStateHandle["hotelId"])

    private val _uiState = MutableStateFlow(savedStateHandle.get<TravelEnterBookingDetailsState>(KEY_STATE) ?: TravelEnterBookingDetailsState())
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
        if (savedStateHandle.get<TravelEnterBookingDetailsState>(KEY_STATE) == null) {
            _uiState.update { it.copy(isLoading = true) }
            viewModelScope.launch {
                val repositoryDraft = hotelBookingDraftRepository.getDraft(hotelId.toString())

                hotelBookingDraft = repositoryDraft ?: HotelBookingDraft(hotelId = hotelId.toString())
                syncSavedState()
                updateUiState()
                delay(500.milliseconds) // Small delay for smooth transition
                updateState { it.copy(isLoading = false) }
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
        if (hotelBookingDraft.checkIn == startDateMillis && hotelBookingDraft.checkOut == endDateMillis) return

        hotelBookingDraft = hotelBookingDraft.copy(
            checkIn = startDateMillis,
            checkOut = endDateMillis,
            roomId = null
        )
        updateState {
            it.copy(
                startDateMillis = startDateMillis,
                endDateMillis = endDateMillis,
                isDateErrorVisible = false
            )
        }
        syncSavedState()
    }

    private fun onAdultsCountChange(count: Int) {
        hotelBookingDraft = hotelBookingDraft.copy(guests = hotelBookingDraft.guests.copy(adults = count))
        updateState {
            it.copy(
                adultsCount = count,
                isGuestErrorVisible = false
            )
        }
        syncSavedState()
    }

    private fun onChildrenCountChange(count: Int) {
        hotelBookingDraft = hotelBookingDraft.copy(guests = hotelBookingDraft.guests.copy(children = count))
        updateState {
            it.copy(childrenCount = count)
        }
        syncSavedState()
    }

    private fun onHasPetsChange(hasPets: Boolean) {
        hotelBookingDraft = hotelBookingDraft.copy(guests = hotelBookingDraft.guests.copy(pets = hasPets))
        updateState {
            it.copy(hasPets = hasPets)
        }
        syncSavedState()
    }

    private fun onNextClick() {
        val validationResult = validateEnterBookingDetailsUseCase.validateDates(
            hotelBookingDraft.checkIn,
            hotelBookingDraft.checkOut
        )

        when (validationResult) {
            is ValidateEnterBookingDetailsUseCase.ValidationResult.Success -> {
                updateState { it.copy(showGuestBottomSheet = true) }
            }

            is ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid -> {
                updateState {
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
        updateState { it.copy(showGuestBottomSheet = false, isGuestErrorVisible = false) }
    }

    private fun onAcceptClick() {
        val validationResult = validateEnterBookingDetailsUseCase.validateGuests(hotelBookingDraft.guests.adults)

        when (validationResult) {
            is ValidateEnterBookingDetailsUseCase.ValidationResult.Success -> {
                // Validation passed for both dates and guests
                updateState { it.copy(showGuestBottomSheet = false) }
                _validationSuccess.value = true
            }

            is ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid -> {
                updateState {
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
        updateState {
            it.copy(
                startDateMillis = hotelBookingDraft.checkIn,
                endDateMillis = hotelBookingDraft.checkOut,
                adultsCount = hotelBookingDraft.guests.adults,
                childrenCount = hotelBookingDraft.guests.children,
                hasPets = hotelBookingDraft.guests.pets
            )
        }
    }

    private fun updateState(update: (TravelEnterBookingDetailsState) -> TravelEnterBookingDetailsState) {
        _uiState.update(update)
        savedStateHandle[KEY_STATE] = _uiState.value
    }

    private fun syncSavedState() {
        savedStateHandle[KEY_BOOKING_DRAFT] = hotelBookingDraft
        viewModelScope.launch {
            hotelBookingDraftRepository.saveDraft(hotelBookingDraft)
        }
    }

    companion object {
        private const val KEY_BOOKING_DRAFT = "hotel_booking_draft"
        private const val KEY_STATE = "booking_details_state"
    }
}
