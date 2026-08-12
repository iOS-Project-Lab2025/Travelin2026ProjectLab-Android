package com.softserveacademy.feature.booking.flight.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.FlightContactInfo
import com.softserveacademy.core.domain.model.FlightPassenger
import com.softserveacademy.core.domain.model.PassengerType
import com.softserveacademy.feature.booking.flight.domain.model.FlightBookingDraft
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingDraftRepository
import com.softserveacademy.feature.booking.flight.domain.usecase.ValidateFlightPassengerInfoUseCase
import com.softserveacademy.feature.booking.flight.presentation.events.FlightPassengerInfoEvent
import com.softserveacademy.feature.booking.flight.presentation.states.FlightPassengerInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Traveler Details screen (US3).
 * Manages individual passenger forms and contact information.
 */
@HiltViewModel
class FlightPassengerInfoViewModel @Inject constructor(
    private val draftRepository: FlightBookingDraftRepository,
    private val validatePassengerInfoUseCase: ValidateFlightPassengerInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlightPassengerInfoState())
    val uiState: StateFlow<FlightPassengerInfoState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val draft = draftRepository.getDraft().filterNotNull().first()

            // If the draft already has passengers, we keep them.
            // Otherwise, we generate the empty list based on the search counts.
            val initialPassengers = if (draft.passengers.isNotEmpty()) {
                draft.passengers
            } else {
                generateEmptyPassengerList(draft)
            }

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
                _uiState.update { current ->
                    val newErrors = current.passengerErrors.toMutableMap().apply { remove(event.index) }
                    current.copy(passengerErrors = newErrors)
                }
            }
            is FlightPassengerInfoEvent.OnPassengerDetailSelected -> {
                updatePassenger(event.index, event.passenger)
                // Al seleccionar un detalle (ej: género), cerramos el modal automáticamente
                _uiState.update { it.copy(showGenderSheet = false, showDocTypeSheet = false, showDatePicker = false) }
            }
            is FlightPassengerInfoEvent.OnContactInfoChanged -> {
                _uiState.update { it.copy(contactInfo = event.contactInfo, contactError = null) }
            }

            // Modals visibility
            is FlightPassengerInfoEvent.OnShowGenderSheet -> _uiState.update {
                it.copy(showGenderSheet = true, activePassengerIndex = event.index)
            }
            is FlightPassengerInfoEvent.OnShowDocTypeSheet -> _uiState.update {
                it.copy(showDocTypeSheet = true, activePassengerIndex = event.index)
            }
            is FlightPassengerInfoEvent.OnShowDatePicker -> _uiState.update {
                it.copy(showDatePicker = true, activePassengerIndex = event.index)
            }
            FlightPassengerInfoEvent.OnDismissSheet -> _uiState.update {
                it.copy(showGenderSheet = false, showDocTypeSheet = false, showDatePicker = false)
            }

            FlightPassengerInfoEvent.OnNextClick -> validateAndSave()
        }
    }

    private fun validateAndSave() {
        val currentState = _uiState.value
        val result = validatePassengerInfoUseCase.validate(
            currentState.passengers,
            currentState.contactInfo
        )

        if (result.isValid) {
            viewModelScope.launch {
                val currentDraft = draftRepository.getDraft().filterNotNull().first()
                val updatedDraft = currentDraft.copy(
                    passengers = currentState.passengers,
                    contactInfo = currentState.contactInfo
                )
                draftRepository.saveDraft(updatedDraft)
                _navigationEvent.emit(Unit)
            }
        } else {
            _uiState.update { it.copy(
                passengerErrors = result.passengerErrors,
                contactError = result.contactError
            )}
        }
    }

    private fun generateEmptyPassengerList(draft: FlightBookingDraft): List<FlightPassenger> {
        val list = mutableListOf<FlightPassenger>()
        repeat(draft.adults) { list.add(FlightPassenger(passengerType = PassengerType.ADU)) }
        repeat(draft.children) { list.add(FlightPassenger(passengerType = PassengerType.CHD)) }
        repeat(draft.infants) { list.add(FlightPassenger(passengerType = PassengerType.INF)) }
        return list
    }

    private fun updatePassenger(index: Int, passenger: FlightPassenger) {
        _uiState.update { current ->
            val newList = current.passengers.toMutableList()
            if (index in newList.indices) newList[index] = passenger
            current.copy(passengers = newList)
        }
    }
}