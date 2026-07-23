package com.softserveacademy.feature.booking.hotel.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.feature.booking.hotel.domain.model.ContactInfo
import com.softserveacademy.feature.booking.hotel.domain.model.HotelBookingDraft
import com.softserveacademy.feature.booking.hotel.domain.repository.HotelBookingDraftRepository
import com.softserveacademy.feature.booking.common.domain.usecase.ValidateContactInfoUseCase
import com.softserveacademy.feature.booking.hotel.presentation.events.HotelContactInfoEvent
import com.softserveacademy.feature.booking.hotel.presentation.states.HotelContactInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotelContactInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val hotelBookingDraftRepository: HotelBookingDraftRepository,
    private val validateContactInfoUseCase: ValidateContactInfoUseCase
) : ViewModel() {

    private val hotelId: Int = checkNotNull(savedStateHandle["hotelId"])

    private val _uiState = MutableStateFlow(savedStateHandle.get<HotelContactInfoState>(KEY_STATE) ?: HotelContactInfoState())
    val uiState: StateFlow<HotelContactInfoState> = _uiState.asStateFlow()

    private val _validationSuccess = MutableStateFlow(false)
    val validationSuccess: StateFlow<Boolean> = _validationSuccess.asStateFlow()

    /**
     * Resets the validation success flag.
     * Should be called when navigation to the next screen is handled or when navigating back.
     */
    fun resetValidationStatus() {
        _validationSuccess.value = false
    }

    private var bookingDraft: HotelBookingDraft? = null

    init {
        if (savedStateHandle.get<HotelContactInfoState>(KEY_STATE) == null) {
            loadBookingDraft()
        }
    }

    private fun loadBookingDraft() {
        viewModelScope.launch {
            bookingDraft = hotelBookingDraftRepository.getDraft(hotelId.toString())
            bookingDraft?.let { draft ->
                updateState {
                    it.copy(
                        firstName = draft.contactInfo.firstName,
                        lastName = draft.contactInfo.lastName,
                        email = draft.contactInfo.email,
                        phoneNumber = draft.contactInfo.phoneNumber
                    )
                }
            }
        }
    }

    fun onEvent(event: HotelContactInfoEvent) {
        when (event) {
            is HotelContactInfoEvent.FirstNameChanged -> {
                updateState { it.copy(firstName = event.firstName, firstNameError = null) }
            }
            is HotelContactInfoEvent.LastNameChanged -> {
                updateState { it.copy(lastName = event.lastName, lastNameError = null) }
            }
            is HotelContactInfoEvent.EmailChanged -> {
                updateState { it.copy(email = event.email, emailError = null) }
            }
            is HotelContactInfoEvent.PhoneNumberChanged -> {
                updateState { it.copy(phoneNumber = event.phoneNumber, phoneNumberError = null) }
            }
            is HotelContactInfoEvent.CountryCodeChanged -> {
                updateState { it.copy(countryCode = event.countryCode) }
            }
            HotelContactInfoEvent.OnNextClick -> {
                validateAndSave()
            }
        }
    }

    private fun updateState(update: (HotelContactInfoState) -> HotelContactInfoState) {
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
            val updatedContactInfo = ContactInfo(
                firstName = _uiState.value.firstName,
                lastName = _uiState.value.lastName,
                email = _uiState.value.email,
                phoneNumber = _uiState.value.phoneNumber
            )
            val updatedDraft = (bookingDraft ?: HotelBookingDraft(hotelId = hotelId.toString())).copy(
                contactInfo = updatedContactInfo
            )
            hotelBookingDraftRepository.saveDraft(updatedDraft)
            _validationSuccess.value = true
        }
    }

    companion object {
        private const val KEY_STATE = "contact_info_state"
    }
}
