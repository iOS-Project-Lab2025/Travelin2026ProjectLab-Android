package com.softserveacademy.feature.booking.tour.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.BookingContactInfo
import com.softserveacademy.feature.booking.common.domain.usecase.ValidateContactInfoUseCase
import com.softserveacademy.feature.booking.common.presentation.events.TravelBookingContactInfoEvent
import com.softserveacademy.feature.booking.common.presentation.states.TravelBookingContactInfoState
import com.softserveacademy.feature.booking.tour.domain.model.TourBookingDraft
import com.softserveacademy.feature.booking.tour.domain.usecase.GetTourBookingDraftUseCase
import com.softserveacademy.feature.booking.tour.domain.usecase.SaveTourBookingDraftUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TourContactInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getTourBookingDraftUseCase: GetTourBookingDraftUseCase,
    private val saveTourBookingDraftUseCase: SaveTourBookingDraftUseCase,
    private val validateContactInfoUseCase: ValidateContactInfoUseCase
) : ViewModel() {

    private val tourId: String = checkNotNull(savedStateHandle["tourId"])

    private val _uiState = MutableStateFlow(savedStateHandle.get<TravelBookingContactInfoState>(KEY_STATE) ?: TravelBookingContactInfoState())
    val uiState: StateFlow<TravelBookingContactInfoState> = _uiState.asStateFlow()

    private val _validationSuccess = MutableStateFlow(false)
    val validationSuccess: StateFlow<Boolean> = _validationSuccess.asStateFlow()

    private var tourBookingDraft: TourBookingDraft? = null

    init {
        if (savedStateHandle.get<TravelBookingContactInfoState>(KEY_STATE) == null) {
            loadBookingDraft()
        }
    }

    private fun loadBookingDraft() {
        updateState { it.copy(isLoading = true) }
        viewModelScope.launch {
            tourBookingDraft = getTourBookingDraftUseCase(tourId)
            tourBookingDraft?.let { draft ->
                updateState {
                    it.copy(
                        firstName = draft.contactInfo.firstName,
                        lastName = draft.contactInfo.lastName,
                        email = draft.contactInfo.email,
                        countryCode = if (draft.contactInfo.countryCode.isNotEmpty()) {
                            draft.contactInfo.countryCode
                        } else {
                            it.countryCode
                        },
                        phoneNumber = draft.contactInfo.phoneNumber
                    )
                }
            }
            updateState { it.copy(isLoading = false) }
        }
    }

    fun onEvent(event: TravelBookingContactInfoEvent) {
        when (event) {
            is TravelBookingContactInfoEvent.FirstNameChanged -> {
                updateState { it.copy(firstName = event.firstName, firstNameError = null) }
            }
            is TravelBookingContactInfoEvent.LastNameChanged -> {
                updateState { it.copy(lastName = event.lastName, lastNameError = null) }
            }
            is TravelBookingContactInfoEvent.EmailChanged -> {
                updateState { it.copy(email = event.email, emailError = null) }
            }
            is TravelBookingContactInfoEvent.PhoneNumberChanged -> {
                updateState { it.copy(phoneNumber = event.phoneNumber, phoneNumberError = null) }
            }
            is TravelBookingContactInfoEvent.CountryCodeChanged -> {
                updateState { it.copy(countryCode = event.countryCode) }
            }
            TravelBookingContactInfoEvent.OnNextClick -> {
                validateAndSave()
            }
            TravelBookingContactInfoEvent.OnBackClick -> { /* Handled by navigation */ }
        }
    }

    private fun updateState(update: (TravelBookingContactInfoState) -> TravelBookingContactInfoState) {
        _uiState.update(update)
        savedStateHandle[KEY_STATE] = _uiState.value
    }

    private fun validateAndSave() {
        val firstNameResult = validateContactInfoUseCase.validateFirstName(_uiState.value.firstName)
        val lastNameResult = validateContactInfoUseCase.validateLastName(_uiState.value.lastName)
        val emailResult = validateContactInfoUseCase.validateEmail(_uiState.value.email)
        val phoneNumberResult = validateContactInfoUseCase.validatePhoneNumber(_uiState.value.phoneNumber)

        val hasError = listOf(firstNameResult, lastNameResult, emailResult, phoneNumberResult)
            .any { it is ValidateContactInfoUseCase.ValidationResult.Invalid }

        if (hasError) {
            _uiState.update { state ->
                state.copy(
                    firstNameError = (firstNameResult as? ValidateContactInfoUseCase.ValidationResult.Invalid)?.errorMessage,
                    lastNameError = (lastNameResult as? ValidateContactInfoUseCase.ValidationResult.Invalid)?.errorMessage,
                    emailError = (emailResult as? ValidateContactInfoUseCase.ValidationResult.Invalid)?.errorMessage,
                    phoneNumberError = (phoneNumberResult as? ValidateContactInfoUseCase.ValidationResult.Invalid)?.errorMessage
                )
            }
            return
        }

        viewModelScope.launch {
            val repositoryDraft = getTourBookingDraftUseCase(tourId)
            val updatedContactInfo = BookingContactInfo(
                firstName = _uiState.value.firstName,
                lastName = _uiState.value.lastName,
                email = _uiState.value.email,
                countryCode = _uiState.value.countryCode,
                phoneNumber = _uiState.value.phoneNumber
            )
            val updatedDraft = (repositoryDraft ?: TourBookingDraft(tourId = tourId)).copy(
                contactInfo = updatedContactInfo
            )
            saveTourBookingDraftUseCase(updatedDraft)
            _validationSuccess.value = true
        }
    }

    fun resetValidationStatus() {
        _validationSuccess.value = false
    }

    companion object {
        private const val KEY_STATE = "contact_info_state"
    }
}
