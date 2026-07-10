package com.softserveacademy.feature.booking.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.softserveacademy.feature.booking.domain.HotelBookingDraft
import com.softserveacademy.feature.booking.domain.ValidateBookingSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * View model for the hotel booking search screen.
 *
 * @property savedStateHandle The handle to saved state.
 */
@HiltViewModel
class HotelBookingSearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val validateBookingSearchUseCase: ValidateBookingSearchUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HotelBookingSearchState())

    /**
     * The current state of the hotel booking search screen.
     */
    val uiState: StateFlow<HotelBookingSearchState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<HotelBookingDraft>(KEY_BOOKING_DRAFT)?.let { draft ->
            _uiState.update { it.copy(draft = draft) }
        }
    }

    /**
     * Handles UI events from the hotel booking search screen.
     *
     * @param event The event to handle.
     */
    fun onEvent(event: HotelBookingSearchEvent) {
        when (event) {
            is HotelBookingSearchEvent.OnDateRangeSelected -> onDateRangeSelected(
                event.startDateMillis,
                event.endDateMillis
            )

            is HotelBookingSearchEvent.OnAdultsCountChange -> onAdultsCountChange(event.count)
            is HotelBookingSearchEvent.OnChildrenCountChange -> onChildrenCountChange(event.count)
            is HotelBookingSearchEvent.OnHasPetsChange -> onHasPetsChange(event.hasPets)
            HotelBookingSearchEvent.OnNextClick -> onNextClick()
            HotelBookingSearchEvent.OnDismissGuestBottomSheet -> onDismissGuestBottomSheet()
            HotelBookingSearchEvent.OnAcceptGuests -> onAcceptGuests()
        }
    }

    private fun onDateRangeSelected(startDateMillis: Long?, endDateMillis: Long?) {
        _uiState.update {
            it.copy(
                draft = it.draft.copy(
                    checkInDate = startDateMillis,
                    checkOutDate = endDateMillis
                ),
                isDateErrorVisible = false
            )
        }
        updateSavedState()
    }

    private fun onAdultsCountChange(count: Int) {
        _uiState.update {
            it.copy(
                draft = it.draft.copy(amountOfAdults = count),
                isGuestErrorVisible = false
            )
        }
        updateSavedState()
    }

    private fun onChildrenCountChange(count: Int) {
        _uiState.update {
            it.copy(draft = it.draft.copy(amountOfChildren = count))
        }
        updateSavedState()
    }

    private fun onHasPetsChange(hasPets: Boolean) {
        _uiState.update {
            it.copy(draft = it.draft.copy(amountOfPets = if (hasPets) 1 else 0))
        }
        updateSavedState()
    }

    private fun onNextClick() {
        val draft = _uiState.value.draft
        val validationResult = validateBookingSearchUseCase.validateDates(
            draft.checkInDate,
            draft.checkOutDate
        )

        when (validationResult) {
            is ValidateBookingSearchUseCase.ValidationResult.Success -> {
                _uiState.update { it.copy(showGuestBottomSheet = true) }
            }

            is ValidateBookingSearchUseCase.ValidationResult.Invalid -> {
                _uiState.update {
                    it.copy(
                        isDateErrorVisible = true,
                        dateErrorMessage = when (validationResult.error) {
                            ValidateBookingSearchUseCase.ValidationError.EMPTY_DATES ->
                                R.string.booking_error_select_dates

                            else -> null
                        }
                    )
                }
            }
        }
    }

    private fun onDismissGuestBottomSheet() {
        _uiState.update { it.copy(showGuestBottomSheet = false, isGuestErrorVisible = false) }
    }

    private fun onAcceptGuests() {
        val draft = _uiState.value.draft
        val validationResult = validateBookingSearchUseCase.validateGuests(draft.amountOfAdults)

        when (validationResult) {
            is ValidateBookingSearchUseCase.ValidationResult.Success -> {
                // Validation passed for both dates and guests
                _uiState.update { it.copy(showGuestBottomSheet = false) }
                // TODO: Navigate to room selection
            }

            is ValidateBookingSearchUseCase.ValidationResult.Invalid -> {
                _uiState.update {
                    it.copy(
                        isGuestErrorVisible = true,
                        guestErrorMessage = when (validationResult.error) {
                            ValidateBookingSearchUseCase.ValidationError.AT_LEAST_ONE_ADULT ->
                                R.string.booking_error_at_least_one_adult

                            else -> null
                        }
                    )
                }
            }
        }
    }

    private fun updateSavedState() {
        savedStateHandle[KEY_BOOKING_DRAFT] = _uiState.value.draft
    }

    companion object {
        private const val KEY_BOOKING_DRAFT = "booking_draft"
    }
}
