package com.softserveacademy.feature.booking.flight.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.FlightContactInfo
import com.softserveacademy.core.domain.model.FlightPassenger
import com.softserveacademy.core.domain.model.PassengerType
import com.softserveacademy.feature.booking.flight.domain.model.FlightBookingDraft
import com.softserveacademy.feature.booking.flight.domain.model.PassengerError
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingDraftRepository
import com.softserveacademy.feature.booking.flight.domain.usecase.ValidateFlightPassengerInfoUseCase
import com.softserveacademy.feature.booking.flight.presentation.events.FlightPassengerInfoEvent
import com.softserveacademy.feature.booking.flight.presentation.states.FlightPassengerInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlightPassengerInfoViewModel @Inject constructor(
    private val draftRepository: FlightBookingDraftRepository,
    private val validatePassengerInfoUseCase: ValidateFlightPassengerInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlightPassengerInfoState())
    val uiState: StateFlow<FlightPassengerInfoState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _navigationBackEvent = MutableSharedFlow<Unit>()
    val navigationBackEvent = _navigationBackEvent.asSharedFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val draft = draftRepository.getDraft().filterNotNull().first()
            val initialPassengers = if (draft.passengers.isNotEmpty()) draft.passengers else generateEmptyPassengerList(draft)

            _uiState.update { it.copy(
                passengers = initialPassengers,
                contactInfo = draft.contactInfo ?: FlightContactInfo(),
                isLoading = false
            )}
        }
    }

    fun onEvent(event: FlightPassengerInfoEvent) {
        when (event) {
            is FlightPassengerInfoEvent.OnPassengerDataChanged -> {
                updatePassenger(event.index, event.passenger)
                // Clear errors as user types
                _uiState.update { it.copy(
                    passengerErrors = it.passengerErrors.toMutableMap().apply { remove(event.index) }
                )}
            }
            is FlightPassengerInfoEvent.OnContactInfoChanged -> {
                _uiState.update { it.copy(contactInfo = event.contactInfo, contactError = null) }
            }
            is FlightPassengerInfoEvent.OnNextClick -> handleNext()
            is FlightPassengerInfoEvent.OnBackClick -> handleBack()

            // Modal Handlers
            is FlightPassengerInfoEvent.OnShowGenderSheet -> _uiState.update { it.copy(showGenderSheet = true, activePassengerIndex = event.index) }
            is FlightPassengerInfoEvent.OnShowDocTypeSheet -> _uiState.update { it.copy(showDocTypeSheet = true, activePassengerIndex = event.index) }
            is FlightPassengerInfoEvent.OnShowDatePicker -> _uiState.update { it.copy(showDatePicker = true, activePassengerIndex = event.index) }
            is FlightPassengerInfoEvent.OnShowNationalitySheet -> _uiState.update { it.copy(showNationalitySheet = true, activePassengerIndex = event.index) }
            FlightPassengerInfoEvent.OnDismissSheet -> _uiState.update { it.copy(showGenderSheet = false, showDocTypeSheet = false, showDatePicker = false, showNationalitySheet = false) }
            is FlightPassengerInfoEvent.OnPassengerDetailSelected -> {
                updatePassenger(event.index, event.passenger)
                _uiState.update { it.copy(showGenderSheet = false, showDocTypeSheet = false, showNationalitySheet = false, showDatePicker = false) }
            }
            is FlightPassengerInfoEvent.OnToggleSameContact -> _uiState.update { it.copy(usePrimaryContact = event.enabled) }
        }
    }

    private fun handleNext() {
        val state = _uiState.value
        val result = validatePassengerInfoUseCase.validate(state.passengers, state.contactInfo)

        val currentPaxError = result.passengerErrors[state.currentPassengerIndex]

        // RULE: Contact info is MANDATORY only for the first passenger step
        val isFirstPaxStep = state.currentPassengerIndex == 0
        val contactHasError = isFirstPaxStep && result.contactError != null

        if (currentPaxError == null && !contactHasError) {
            if (state.currentPassengerIndex < state.passengers.size - 1) {
                // Advance to next passenger wizard step
                _uiState.update { it.copy(currentPassengerIndex = it.currentPassengerIndex + 1) }
            } else {
                // Last passenger completed and valid -> Save and Proceed
                saveAndFinish()
            }
        } else {
            // Show errors for current context
            _uiState.update { it.copy(
                passengerErrors = mapOf(state.currentPassengerIndex to (currentPaxError ?: PassengerError())),
                contactError = if (isFirstPaxStep) result.contactError else it.contactError
            )}
        }
    }

    private fun handleBack() {
        val state = _uiState.value
        if (state.currentPassengerIndex > 0) {
            _uiState.update { it.copy(currentPassengerIndex = it.currentPassengerIndex - 1) }
        } else {
            viewModelScope.launch { _navigationBackEvent.emit(Unit) }
        }
    }

    private fun saveAndFinish() {
        viewModelScope.launch {
            val state = _uiState.value
            val draft = draftRepository.getDraft().filterNotNull().first()
            draftRepository.saveDraft(draft.copy(passengers = state.passengers, contactInfo = state.contactInfo))
            _navigationEvent.emit(Unit)
        }
    }

    private fun updatePassenger(index: Int, passenger: FlightPassenger) {
        _uiState.update { current ->
            val newList = current.passengers.toMutableList()
            if (index in newList.indices) newList[index] = passenger
            current.copy(passengers = newList)
        }
    }

    private fun generateEmptyPassengerList(draft: FlightBookingDraft): List<FlightPassenger> {
        val list = mutableListOf<FlightPassenger>()
        repeat(draft.adults) { list.add(FlightPassenger(passengerType = PassengerType.ADU)) }
        repeat(draft.children) { list.add(FlightPassenger(passengerType = PassengerType.CHD)) }
        repeat(draft.infants) { list.add(FlightPassenger(passengerType = PassengerType.INF)) }
        return list
    }
}