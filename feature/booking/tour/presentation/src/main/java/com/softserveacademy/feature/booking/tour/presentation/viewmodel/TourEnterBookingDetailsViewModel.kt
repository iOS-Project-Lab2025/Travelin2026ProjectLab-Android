package com.softserveacademy.feature.booking.tour.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.usecase.tour.GetTourDetailsUseCase
import com.softserveacademy.core.error.extension.onSuccess
import com.softserveacademy.feature.booking.common.domain.usecase.ValidateEnterBookingDetailsUseCase
import com.softserveacademy.feature.booking.tour.presentation.R as tourBookingR
import com.softserveacademy.feature.booking.common.presentation.R
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent
import com.softserveacademy.feature.booking.tour.presentation.events.TourEnterBookingDetailsEvent
import com.softserveacademy.feature.booking.tour.presentation.states.TourEnterBookingDetailsState
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
import kotlin.time.Duration
import kotlin.time.DurationUnit

@HiltViewModel
class TourEnterBookingDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val validateEnterBookingDetailsUseCase: ValidateEnterBookingDetailsUseCase,
    private val getTourBookingDraftUseCase: GetTourBookingDraftUseCase,
    private val saveTourBookingDraftUseCase: SaveTourBookingDraftUseCase,
    private val getTourDetailsUseCase: GetTourDetailsUseCase
) : ViewModel() {

    private val tourId: String = checkNotNull(savedStateHandle["tourId"])

    private val _uiState = MutableStateFlow(TourEnterBookingDetailsState())
    val uiState: StateFlow<TourEnterBookingDetailsState> = _uiState.asStateFlow()

    private val _validationSuccess = MutableSharedFlow<Boolean>()
    val validationSuccess: SharedFlow<Boolean> = _validationSuccess.asSharedFlow()

    var tourDuration: Duration = Duration.ZERO
    var singleDateSelection: Boolean = false

    private var tourBookingDraft: TourBookingDraft = TourBookingDraft(tourId = tourId)

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(screenState = it.screenState.copy(isLoading = true)) }
            val draft = getTourBookingDraftUseCase(tourId)
            if (draft != null) {
                tourBookingDraft = draft
                getTourDetailsUseCase(tourId)
                    .onSuccess { tourDetails ->
                        // If tour duration is less than 24 hrs enable single date selection
                        tourDuration = tourDetails.duration
                        singleDateSelection = tourDuration <= Duration.parse("PT24H")
                    }
                _uiState.update { state ->
                    state.copy(
                        screenState = state.screenState.copy(
                            startDateMillis = draft.startDate,
                            endDateMillis = draft.endDate,
                            singleDatePicker = singleDateSelection,
                            isLoading = false
                        ),
                        adultsCount = draft.participants.adults,
                        childrenCount = draft.participants.children,
                        infantsCount = draft.participants.infants
                    )
                }
            } else {
                _uiState.update { it.copy(screenState = it.screenState.copy(isLoading = false)) }
            }
        }
    }

    fun onEvent(event: TourEnterBookingDetailsEvent) {
        when (event) {
            is TourEnterBookingDetailsEvent.OnAdultsCountChange -> onAdultsCountChange(event.count)
            is TourEnterBookingDetailsEvent.OnChildrenCountChange -> onChildrenCountChange(event.count)
            is TourEnterBookingDetailsEvent.OnInfantsCountChange -> onInfantsCountChange(event.count)
            is TourEnterBookingDetailsEvent.ScreenEvent -> {
                when (val travelEvent = event.event) {
                    is TravelEnterBookingDetailsEvent.OnDateRangeSelected -> onDateRangeSelected(
                        travelEvent.startDateMillis,
                        travelEvent.endDateMillis
                    )
                    TravelEnterBookingDetailsEvent.OnNextClick -> onNextClick()
                    TravelEnterBookingDetailsEvent.OnDismissBottomSheet -> onDismissBottomSheet()
                    TravelEnterBookingDetailsEvent.OnAcceptClick -> onAcceptClick()
                    TravelEnterBookingDetailsEvent.OnBackClick -> {}
                }
            }
        }
    }

    private fun onDateRangeSelected(startDate: Long?, endDate: Long?) {
        var endDateCorrected = endDate
        if (singleDateSelection){
           endDateCorrected = startDate
        } else if (startDate != null && tourDuration != Duration.ZERO) {
            // Fix the date to ensure it is within the tour duration
            val dateRangeDuration = if (endDateCorrected != null) endDateCorrected - startDate else 0L
            val tourDurationWithoutStart = if (tourDuration != Duration.ZERO) tourDuration - Duration.parse("PT24H") else Duration.ZERO
            if (dateRangeDuration != tourDurationWithoutStart.toLong(DurationUnit.MILLISECONDS)){
                endDateCorrected = startDate + tourDurationWithoutStart.toLong(DurationUnit.MILLISECONDS)
            }
        }
        _uiState.update { it.copy(screenState = it.screenState.copy(startDateMillis = startDate, endDateMillis = endDateCorrected, isDateErrorVisible = false)) }
        tourBookingDraft = tourBookingDraft.copy(startDate = startDate, endDate = endDateCorrected)
        viewModelScope.launch { saveTourBookingDraftUseCase(tourBookingDraft) }
    }

    private fun onAdultsCountChange(count: Int) {
        _uiState.update { it.copy(adultsCount = count) }
    }

    private fun onChildrenCountChange(count: Int) {
        _uiState.update { it.copy(childrenCount = count) }
    }

    private fun onInfantsCountChange(count: Int) {
        _uiState.update { it.copy(infantsCount = count) }
    }

    private fun onNextClick() {
        val dateResult = validateEnterBookingDetailsUseCase.validateDates(uiState.value.screenState.startDateMillis, uiState.value.screenState.endDateMillis)
        if (dateResult is ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid) {
            _uiState.update { it.copy(screenState = it.screenState.copy(isDateErrorVisible = true, dateErrorMessage = tourBookingR.string.error_select_dates)) }
            return
        }
        val durationResult = validateEnterBookingDetailsUseCase.validateDatesDuration(
            uiState.value.screenState.startDateMillis,
            uiState.value.screenState.endDateMillis,
            if (singleDateSelection) tourDuration else tourDuration - Duration.parse("PT24H")
        )
        if (durationResult is ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid) {
            _uiState.update { it.copy(screenState = it.screenState.copy(isDateErrorVisible = true, dateErrorMessage = tourBookingR.string.error_dates_duration)) }
            return
        }
        _uiState.update { it.copy(screenState = it.screenState.copy(showGuestBottomSheet = true)) }
    }

    private fun onDismissBottomSheet() {
        _uiState.update { it.copy(screenState = it.screenState.copy(showGuestBottomSheet = false)) }
    }

    private fun onAcceptClick() {
        val guestResult = validateEnterBookingDetailsUseCase.validateGuests(uiState.value.adultsCount)
        if (guestResult is ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid) {
            _uiState.update { it.copy(screenState = it.screenState.copy(isGuestErrorVisible = true, guestErrorMessage = R.string.booking_error_at_least_one_adult)) }
            return
        }

        tourBookingDraft = tourBookingDraft.copy(
            participants = Participants(
                adults = uiState.value.adultsCount,
                children = uiState.value.childrenCount,
                infants = uiState.value.infantsCount
            )
        )

        viewModelScope.launch {
            saveTourBookingDraftUseCase(tourBookingDraft)
            _uiState.update { it.copy(screenState = it.screenState.copy(showGuestBottomSheet = false)) }
            _validationSuccess.emit(true)
        }
    }

    fun resetValidationStatus(){
        // Reset the validation status
    }
}
