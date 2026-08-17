package com.softserveacademy.feature.booking.tour.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.feature.booking.common.domain.usecase.ValidateEnterBookingDetailsUseCase
import com.softserveacademy.feature.booking.common.presentation.R
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent
import com.softserveacademy.feature.booking.common.presentation.states.TravelEnterBookingDetailsState
import com.softserveacademy.feature.booking.tour.domain.model.Participants
import com.softserveacademy.feature.booking.tour.domain.model.TourBookingDraft
import com.softserveacademy.feature.booking.tour.domain.usecase.GetTourBookingDraftUseCase
import com.softserveacademy.feature.booking.tour.domain.usecase.SaveTourBookingDraftUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TourEnterBookingDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val validateEnterBookingDetailsUseCase: ValidateEnterBookingDetailsUseCase,
    private val getTourBookingDraftUseCase: GetTourBookingDraftUseCase,
    private val saveTourBookingDraftUseCase: SaveTourBookingDraftUseCase
) : ViewModel() {

    private val tourId: String = checkNotNull(savedStateHandle["tourId"])

    private val _uiState = MutableStateFlow(TravelEnterBookingDetailsState())
    val uiState: StateFlow<TravelEnterBookingDetailsState> = _uiState.asStateFlow()

    private val _validationSuccess = MutableSharedFlow<Boolean>()
    val validationSuccess: SharedFlow<Boolean> = _validationSuccess.asSharedFlow()

    private var tourBookingDraft: TourBookingDraft = TourBookingDraft(tourId = tourId)

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val draft = getTourBookingDraftUseCase(tourId)
            if (draft != null) {
                tourBookingDraft = draft
                _uiState.update { state ->
                    state.copy(
                        startDateMillis = draft.startDate,
                        endDateMillis = draft.endDate,
                        adultsCount = draft.participants.adults,
                        childrenCount = draft.participants.children,
                        babiesCount = draft.participants.babies,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onEvent(event: TravelEnterBookingDetailsEvent) {
        when (event) {
            is TravelEnterBookingDetailsEvent.OnDateRangeSelected -> onDateRangeSelected(event.startDateMillis, event.endDateMillis)
            is TravelEnterBookingDetailsEvent.OnAdultsCountChange -> onAdultsCountChange(event.count)
            is TravelEnterBookingDetailsEvent.OnChildrenCountChange -> onChildrenCountChange(event.count)
            is TravelEnterBookingDetailsEvent.OnBabiesCountChange -> onBabiesCountChange(event.count)
            TravelEnterBookingDetailsEvent.OnNextClick -> onNextClick()
            TravelEnterBookingDetailsEvent.OnDismissBottomSheet -> onDismissBottomSheet()
            TravelEnterBookingDetailsEvent.OnAcceptClick -> onAcceptClick()
            else -> {}
        }
    }

    private fun onDateRangeSelected(startDate: Long?, endDate: Long?) {
        _uiState.update { it.copy(startDateMillis = startDate, endDateMillis = endDate, isDateErrorVisible = false) }
        tourBookingDraft = tourBookingDraft.copy(startDate = startDate, endDate = endDate)
        viewModelScope.launch { saveTourBookingDraftUseCase(tourBookingDraft) }
    }

    private fun onAdultsCountChange(count: Int) {
        _uiState.update { it.copy(adultsCount = count) }
    }

    private fun onChildrenCountChange(count: Int) {
        _uiState.update { it.copy(childrenCount = count) }
    }

    private fun onBabiesCountChange(count: Int) {
        _uiState.update { it.copy(babiesCount = count) }
    }

    private fun onNextClick() {
        val dateResult = validateEnterBookingDetailsUseCase.validateDates(uiState.value.startDateMillis, uiState.value.endDateMillis)
        if (dateResult is ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid) {
            _uiState.update { it.copy(isDateErrorVisible = true, dateErrorMessage = R.string.booking_error_select_dates) }
            return
        }
        _uiState.update { it.copy(showGuestBottomSheet = true) }
    }

    private fun onDismissBottomSheet() {
        _uiState.update { it.copy(showGuestBottomSheet = false) }
    }

    private fun onAcceptClick() {
        val guestResult = validateEnterBookingDetailsUseCase.validateGuests(uiState.value.adultsCount)
        if (guestResult is ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid) {
            _uiState.update { it.copy(isGuestErrorVisible = true, guestErrorMessage = R.string.booking_error_at_least_one_adult) }
            return
        }

        tourBookingDraft = tourBookingDraft.copy(
            participants = Participants(
                adults = uiState.value.adultsCount,
                children = uiState.value.childrenCount,
                babies = uiState.value.babiesCount
            )
        )

        viewModelScope.launch {
            saveTourBookingDraftUseCase(tourBookingDraft)
            _uiState.update { it.copy(showGuestBottomSheet = false) }
            _validationSuccess.emit(true)
        }
    }

    fun resetValidationStatus() {
        // No-op for now, matched hotel implementation but we use SharedFlow
    }
}
