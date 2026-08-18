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
import com.softserveacademy.feature.booking.tour.domain.model.TourBookingDraft
import com.softserveacademy.feature.booking.tour.domain.usecase.GetTourBookingDraftUseCase
import com.softserveacademy.feature.booking.tour.domain.usecase.SaveTourBookingDraftUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
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

    private val _uiState = MutableStateFlow(savedStateHandle.get<TourEnterBookingDetailsState>(KEY_STATE) ?: TourEnterBookingDetailsState())
    val uiState: StateFlow<TourEnterBookingDetailsState> = _uiState.asStateFlow()

    private val _validationSuccess = MutableStateFlow(false)
    val validationSuccess: StateFlow<Boolean> = _validationSuccess.asStateFlow()

    var tourDuration: Duration = Duration.ZERO
    var singleDateSelection: Boolean = false

    private var tourBookingDraft: TourBookingDraft = savedStateHandle.get<TourBookingDraft>(KEY_BOOKING_DRAFT) ?: TourBookingDraft(tourId = tourId)

    init {
        if (savedStateHandle.get<TourEnterBookingDetailsState>(KEY_STATE) == null) {
            updateState { it.copy(screenState = it.screenState.copy(isLoading = true)) }
            viewModelScope.launch {
                val draft = getTourBookingDraftUseCase(tourId)
                if (draft != null) {
                    tourBookingDraft = draft
                }

                getTourDetailsUseCase(tourId)
                    .onSuccess { tourDetails ->
                        // If tour duration is less than 24 hrs enable single date selection
                        tourDuration = tourDetails.duration
                        singleDateSelection = tourDuration <= Duration.parse("PT24H")
                    }

                syncSavedState()
                updateUiState()
                delay(500.milliseconds) // Small delay for smooth transition
                updateState { it.copy(screenState = it.screenState.copy(isLoading = false)) }
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
        if (singleDateSelection) {
            endDateCorrected = startDate
        } else if (startDate != null && tourDuration != Duration.ZERO) {
            // Fix the date to ensure it is within the tour duration
            val dateRangeDuration = if (endDateCorrected != null) endDateCorrected - startDate else 0L
            val tourDurationWithoutStart =
                if (tourDuration != Duration.ZERO) tourDuration - Duration.parse("PT24H") else Duration.ZERO
            if (dateRangeDuration != tourDurationWithoutStart.toLong(DurationUnit.MILLISECONDS)) {
                endDateCorrected =
                    startDate + tourDurationWithoutStart.toLong(DurationUnit.MILLISECONDS)
            }
        }

        if (tourBookingDraft.startDate == startDate && tourBookingDraft.endDate == endDateCorrected) return

        tourBookingDraft = tourBookingDraft.copy(startDate = startDate, endDate = endDateCorrected)
        updateState {
            it.copy(
                screenState = it.screenState.copy(
                    startDateMillis = startDate,
                    endDateMillis = endDateCorrected,
                    isDateErrorVisible = false
                )
            )
        }
        syncSavedState()
    }

    private fun onAdultsCountChange(count: Int) {
        tourBookingDraft =
            tourBookingDraft.copy(participants = tourBookingDraft.participants.copy(adults = count))
        updateState {
            it.copy(
                adultsCount = count,
                screenState = it.screenState.copy(isGuestErrorVisible = false)
            )
        }
        syncSavedState()
    }

    private fun onChildrenCountChange(count: Int) {
        tourBookingDraft =
            tourBookingDraft.copy(participants = tourBookingDraft.participants.copy(children = count))
        updateState { it.copy(childrenCount = count) }
        syncSavedState()
    }

    private fun onInfantsCountChange(count: Int) {
        tourBookingDraft =
            tourBookingDraft.copy(participants = tourBookingDraft.participants.copy(infants = count))
        updateState { it.copy(infantsCount = count) }
        syncSavedState()
    }

    private fun onNextClick() {
        val dateResult = validateEnterBookingDetailsUseCase.validateDates(
            uiState.value.screenState.startDateMillis,
            uiState.value.screenState.endDateMillis
        )
        if (dateResult is ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid) {
            updateState {
                it.copy(
                    screenState = it.screenState.copy(
                        isDateErrorVisible = true,
                        dateErrorMessage = tourBookingR.string.error_select_dates
                    )
                )
            }
            return
        }
        val durationResult = validateEnterBookingDetailsUseCase.validateDatesDuration(
            uiState.value.screenState.startDateMillis,
            uiState.value.screenState.endDateMillis,
            if (singleDateSelection) tourDuration else tourDuration - Duration.parse("PT24H")
        )
        if (durationResult is ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid) {
            updateState {
                it.copy(
                    screenState = it.screenState.copy(
                        isDateErrorVisible = true,
                        dateErrorMessage = tourBookingR.string.error_dates_duration
                    )
                )
            }
            return
        }
        updateState { it.copy(screenState = it.screenState.copy(showGuestBottomSheet = true)) }
    }

    private fun onDismissBottomSheet() {
        updateState {
            it.copy(
                screenState = it.screenState.copy(
                    showGuestBottomSheet = false,
                    isGuestErrorVisible = false
                )
            )
        }
    }

    private fun onAcceptClick() {
        val guestResult = validateEnterBookingDetailsUseCase.validateGuests(uiState.value.adultsCount)
        if (guestResult is ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid) {
            updateState {
                it.copy(
                    screenState = it.screenState.copy(
                        isGuestErrorVisible = true,
                        guestErrorMessage = R.string.booking_error_at_least_one_adult
                    )
                )
            }
            return
        }

        updateState { it.copy(screenState = it.screenState.copy(showGuestBottomSheet = false)) }
        _validationSuccess.value = true
    }

    private fun updateUiState() {
        updateState {
            it.copy(
                screenState = it.screenState.copy(
                    startDateMillis = tourBookingDraft.startDate,
                    endDateMillis = tourBookingDraft.endDate,
                    singleDatePicker = singleDateSelection
                ),
                adultsCount = tourBookingDraft.participants.adults,
                childrenCount = tourBookingDraft.participants.children,
                infantsCount = tourBookingDraft.participants.infants
            )
        }
    }

    private fun updateState(update: (TourEnterBookingDetailsState) -> TourEnterBookingDetailsState) {
        _uiState.update(update)
        savedStateHandle[KEY_STATE] = _uiState.value
    }

    private fun syncSavedState() {
        savedStateHandle[KEY_BOOKING_DRAFT] = tourBookingDraft
        viewModelScope.launch {
            saveTourBookingDraftUseCase(tourBookingDraft)
        }
    }

    /**
     * Resets the validation success flag.
     * Should be called when navigation to the next screen is handled or when navigating back.
     */
    fun resetValidationStatus() {
        _validationSuccess.value = false
    }

    companion object {
        private const val KEY_BOOKING_DRAFT = "tour_booking_draft"
        private const val KEY_STATE = "booking_details_state"
    }
}
