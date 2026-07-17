package com.softserveacademy.feature.booking.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.feature.booking.domain.repository.BookingRepository
import com.softserveacademy.feature.booking.domain.model.HotelBookingDraft
import com.softserveacademy.feature.booking.domain.ValidateBookingSearchUseCase
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
 * View model for the hotel booking search screen.
 *
 * @property savedStateHandle The handle to saved state.
 * @property validateBookingSearchUseCase Use case for validating booking search input.
 * @property bookingRepository Repository for persisting booking drafts.
 */
@HiltViewModel
class HotelBookingSearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val validateBookingSearchUseCase: ValidateBookingSearchUseCase,
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TravelBookingSearchState())

    /**
     * The current state of the hotel booking search screen.
     */
    val uiState: StateFlow<TravelBookingSearchState> = _uiState.asStateFlow()
    
    private val _validationSuccess = MutableStateFlow(false)
    val validationSuccess: StateFlow<Boolean> = _validationSuccess.asStateFlow()

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
     * Handles UI events from the hotel booking search screen.
     *
     * @param event The event to handle.
     */
    fun onEvent(event: TravelBookingSearchEvent) {
        when (event) {
            is TravelBookingSearchEvent.OnDateRangeSelected -> onDateRangeSelected(
                event.startDateMillis,
                event.endDateMillis
            )

            is TravelBookingSearchEvent.OnAdultsCountChange -> onAdultsCountChange(event.count)
            is TravelBookingSearchEvent.OnChildrenCountChange -> onChildrenCountChange(event.count)
            is TravelBookingSearchEvent.OnHasPetsChange -> onHasPetsChange(event.hasPets)
            TravelBookingSearchEvent.OnNextClick -> onNextClick()
            TravelBookingSearchEvent.OnBackClick -> { /* Handled by navigation */ }
            TravelBookingSearchEvent.OnDismissGuestBottomSheet -> onDismissGuestBottomSheet()
            TravelBookingSearchEvent.OnAcceptGuests -> onAcceptGuests()
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
        val validationResult = validateBookingSearchUseCase.validateDates(
            hotelBookingDraft.checkInDate,
            hotelBookingDraft.checkOutDate
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
        val validationResult = validateBookingSearchUseCase.validateGuests(hotelBookingDraft.amountOfAdults)

        when (validationResult) {
            is ValidateBookingSearchUseCase.ValidationResult.Success -> {
                // Validation passed for both dates and guests
                _uiState.update { it.copy(showGuestBottomSheet = false) }
                _validationSuccess.value = true
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
