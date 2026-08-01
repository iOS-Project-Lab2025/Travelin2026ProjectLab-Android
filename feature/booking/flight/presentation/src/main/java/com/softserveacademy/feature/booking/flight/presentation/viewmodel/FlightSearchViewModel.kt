package com.softserveacademy.feature.booking.flight.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent
import com.softserveacademy.feature.booking.flight.domain.model.FlightBookingDraft
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingDraftRepository
import com.softserveacademy.feature.booking.flight.domain.usecase.SearchAirportsUseCase
import com.softserveacademy.feature.booking.flight.domain.usecase.ValidateFlightSearchUseCase // NUEVO
import com.softserveacademy.feature.booking.flight.domain.usecase.ValidateFlightSearchUseCase.FlightValidationResult // NUEVO
import com.softserveacademy.feature.booking.flight.presentation.R
import com.softserveacademy.feature.booking.flight.presentation.events.FlightSearchEvent
import com.softserveacademy.feature.booking.flight.presentation.states.FlightSearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for flight searching criteria.
 * Acts as an orchestrator for input security, business validation, and draft persistence.
 */
@HiltViewModel
class FlightSearchViewModel @Inject constructor(
    private val searchAirportsUseCase: SearchAirportsUseCase,
    private val validateFlightSearchUseCase: ValidateFlightSearchUseCase, // 1. INYECTADO (Línea 21)
    private val draftRepository: FlightBookingDraftRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlightSearchState())
    val uiState: StateFlow<FlightSearchState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    /**
     * Handles all UI intents.
     */
    fun onEvent(event: FlightSearchEvent) {
        when (event) {
            is FlightSearchEvent.OnOriginQueryChanged -> {

                _uiState.update { it.copy(originQuery = event.query, errorMessage = null) }
                searchAirports(event.query, isOrigin = true)
            }
            is FlightSearchEvent.OnDestinationQueryChanged -> {
                _uiState.update { it.copy(destinationQuery = event.query, errorMessage = null) }
                searchAirports(event.query, isOrigin = false)
            }
            is FlightSearchEvent.OnOriginSelected -> {
                _uiState.update { it.copy(originQuery = event.airport.code, originSuggestions = emptyList()) }
            }
            is FlightSearchEvent.OnDestinationSelected -> {
                _uiState.update { it.copy(destinationQuery = event.airport.code, destinationSuggestions = emptyList()) }
            }
            is FlightSearchEvent.OnShowPassengerSheet -> {
                _uiState.update { it.copy(bookingDetailsState = it.bookingDetailsState.copy(showGuestBottomSheet = true)) }
            }
            is FlightSearchEvent.OnFlightTypeSelected -> _uiState.update { it.copy(selectedFlightType = event.flightType) }
            is FlightSearchEvent.OnCabinClassSelected -> _uiState.update { it.copy(selectedCabinClass = event.cabinClass, showCabinSheet = false) }
            is FlightSearchEvent.OnShowCabinSheet -> _uiState.update { it.copy(showCabinSheet = true) }
            is FlightSearchEvent.OnDismissCabinSheet -> _uiState.update { it.copy(showCabinSheet = false) }
            is FlightSearchEvent.OnAdultsChanged -> _uiState.update { it.copy(adults = event.count) }
            is FlightSearchEvent.OnChildrenChanged -> _uiState.update { it.copy(children = event.count) }
            is FlightSearchEvent.OnInfantsChanged -> _uiState.update { it.copy(infants = event.count) }
            is FlightSearchEvent.InternalBookingEvent -> handleInternalEvent(event.event)
            is FlightSearchEvent.OnSwapLocations -> {
                _uiState.update { current ->
                    current.copy(
                        originQuery = current.destinationQuery,
                        destinationQuery = current.originQuery
                    )
                }
            }
            FlightSearchEvent.OnPerformSearch -> saveDraftAndNavigate() // 2. VALIDACIÓN AQUÍ (Línea 56)
        }
    }

    private fun handleInternalEvent(event: TravelEnterBookingDetailsEvent) {
        _uiState.update { current ->
            val newState = when (event) {
                is TravelEnterBookingDetailsEvent.OnDateRangeSelected ->
                    current.bookingDetailsState.copy(
                        startDateMillis = event.startDateMillis,
                        endDateMillis = event.endDateMillis
                    )
                TravelEnterBookingDetailsEvent.OnAcceptClick, TravelEnterBookingDetailsEvent.OnDismissBottomSheet ->
                    current.bookingDetailsState.copy(showGuestBottomSheet = false)
                else -> current.bookingDetailsState
            }
            current.copy(bookingDetailsState = newState)
        }
    }

    /**
     * Logic to validate, persist and trigger navigation.
     * Implements security checks before saving anything to disk.
     */
    private fun saveDraftAndNavigate() {
        val currentState = _uiState.value

        // 3. EJECUTAR VALIDACIÓN DE SEGURIDAD Y NEGOCIO
        val validationResult = validateFlightSearchUseCase.validate(
            origin = currentState.originQuery,
            destination = currentState.destinationQuery,
            startDate = currentState.bookingDetailsState.startDateMillis,
            adults = currentState.adults

        )

        when (validationResult) {
            is FlightValidationResult.Success -> {
                viewModelScope.launch {
                    val draft = FlightBookingDraft(
                        origin = currentState.originQuery.trim().uppercase(),
                        destination = currentState.destinationQuery.trim().uppercase(),
                        startDateMillis = currentState.bookingDetailsState.startDateMillis,
                        endDateMillis = currentState.bookingDetailsState.endDateMillis,
                        adults = currentState.adults,
                        children = currentState.children,
                        infants = currentState.infants
                    )
                    draftRepository.saveDraft(draft)
                    _navigationEvent.emit(Unit)
                }
            }
            else -> {
                // 4. MAPEO DE ERRORES AL UI STATE (Línea 112)
                _uiState.update { it.copy(errorMessage = mapValidationError(validationResult)) }
            }
        }
    }

    private fun mapValidationError(result: FlightValidationResult): Int {
        return when (result) {
            is FlightValidationResult.InvalidOrigin -> R.string.flight_error_invalid_origin
            is FlightValidationResult.InvalidDestination -> R.string.flight_error_invalid_destination
            is FlightValidationResult.InvalidDate -> R.string.flight_error_invalid_date
            is FlightValidationResult.InvalidPassengers -> R.string.flight_error_no_adults
            FlightValidationResult.SameLocation -> R.string.flight_error_same_location
            else -> R.string.flight_error_generic
        }
    }

    private fun searchAirports(query: String, isOrigin: Boolean) {
        viewModelScope.launch {
            searchAirportsUseCase(query).collect { list ->
                _uiState.update {
                    if (isOrigin) it.copy(originSuggestions = list)
                    else it.copy(destinationSuggestions = list)
                }
            }
        }
    }
}
