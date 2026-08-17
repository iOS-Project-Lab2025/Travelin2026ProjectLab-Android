package com.softserveacademy.feature.booking.tour.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.BookingContactInfo
import com.softserveacademy.feature.booking.common.presentation.events.TravelBookingContactInfoEvent
import com.softserveacademy.feature.booking.common.presentation.states.TravelBookingContactInfoState
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
class TourContactInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getTourBookingDraftUseCase: GetTourBookingDraftUseCase,
    private val saveTourBookingDraftUseCase: SaveTourBookingDraftUseCase
) : ViewModel() {

    private val tourId: String = checkNotNull(savedStateHandle["tourId"])

    private val _uiState = MutableStateFlow(TravelBookingContactInfoState())
    val uiState: StateFlow<TravelBookingContactInfoState> = _uiState.asStateFlow()

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
                        firstName = draft.contactInfo.firstName,
                        lastName = draft.contactInfo.lastName,
                        email = draft.contactInfo.email,
                        countryCode = draft.contactInfo.countryCode,
                        phoneNumber = draft.contactInfo.phoneNumber,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onEvent(event: TravelBookingContactInfoEvent) {
        when (event) {
            is TravelBookingContactInfoEvent.FirstNameChanged -> _uiState.update { it.copy(firstName = event.firstName) }
            is TravelBookingContactInfoEvent.LastNameChanged -> _uiState.update { it.copy(lastName = event.lastName) }
            is TravelBookingContactInfoEvent.EmailChanged -> _uiState.update { it.copy(email = event.email) }
            is TravelBookingContactInfoEvent.CountryCodeChanged -> _uiState.update { it.copy(countryCode = event.countryCode) }
            is TravelBookingContactInfoEvent.PhoneNumberChanged -> _uiState.update { it.copy(phoneNumber = event.phoneNumber) }
            TravelBookingContactInfoEvent.OnNextClick -> onNextClick()
            else -> {}
        }
    }

    private fun onNextClick() {
        // Simple validation
        if (uiState.value.firstName.isBlank() || uiState.value.lastName.isBlank() || uiState.value.email.isBlank()) {
            // Error handling could be improved
            return
        }

        tourBookingDraft = tourBookingDraft.copy(
            contactInfo = BookingContactInfo(
                firstName = uiState.value.firstName,
                lastName = uiState.value.lastName,
                email = uiState.value.email,
                countryCode = uiState.value.countryCode,
                phoneNumber = uiState.value.phoneNumber
            )
        )

        viewModelScope.launch {
            saveTourBookingDraftUseCase(tourBookingDraft)
            _validationSuccess.emit(true)
        }
    }

    fun resetValidationStatus() {
        // No-op for now
    }
}
