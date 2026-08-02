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
                updateSegment(event.index) { it.copy(origin = event.query) }
                searchAirports(event.query, isOrigin = true, index = event.index)
            }
            is FlightSearchEvent.OnDestinationQueryChanged -> {
                updateSegment(event.index) { it.copy(destination = event.query) }
                searchAirports(event.query, isOrigin = false, index = event.index)
            }
            is FlightSearchEvent.OnOriginSelected -> {
                updateSegment(event.index) { it.copy(origin = event.airport.code) }
                _uiState.update { it.copy(originSuggestions = emptyList()) }
            }
            is FlightSearchEvent.OnDestinationSelected -> {
                updateSegment(event.index) { it.copy(destination = event.airport.code) }
                _uiState.update { it.copy(destinationSuggestions = emptyList()) }
            }

            is FlightSearchEvent.OnDateSelected -> {
                updateSegment(event.index) { it.copy(dateMillis = event.dateMillis) }
                // SI ES EL PRIMER VUELO, sincronizamos con el calendario global
                if (event.index == 0) {
                    _uiState.update { current ->
                        current.copy(
                            bookingDetailsState = current.bookingDetailsState.copy(
                                startDateMillis = event.dateMillis
                            )
                        )
                    }
                }
            }

            is FlightSearchEvent.OnShowPassengerSheet -> {
                _uiState.update { it.copy(bookingDetailsState = it.bookingDetailsState.copy(showGuestBottomSheet = true)) }
            }
            is FlightSearchEvent.OnFlightTypeSelected -> {
                _uiState.update { it.copy(
                    selectedFlightType = event.flightType,
                    // Si pasa a Multi-city, aseguramos al menos 2 tramos
                    segments = if (event.flightType == com.softserveacademy.core.domain.model.FlightType.MULTI_CITY && it.segments.size < 2)
                        listOf(it.segments[0], com.softserveacademy.core.domain.model.FlightSegment())
                    else it.segments.take(1) // Si vuelve a One Way/Round Trip, dejamos solo el primero
                )}
            }
            is FlightSearchEvent.OnCabinClassSelected -> {
                _uiState.update { it.copy(selectedCabinClass = event.cabinClass, showCabinSheet = false) }
            }
            is FlightSearchEvent.OnShowCabinSheet -> _uiState.update { it.copy(showCabinSheet = true) }
            is FlightSearchEvent.OnDismissCabinSheet -> _uiState.update { it.copy(showCabinSheet = false) }
            is FlightSearchEvent.OnAdultsChanged -> _uiState.update { it.copy(adults = event.count) }
            is FlightSearchEvent.OnChildrenChanged -> _uiState.update { it.copy(children = event.count) }
            is FlightSearchEvent.OnInfantsChanged -> _uiState.update { it.copy(infants = event.count) }
            is FlightSearchEvent.InternalBookingEvent -> handleInternalEvent(event.event)
            is FlightSearchEvent.OnSwapSegmentLocations -> {
                updateSegment(event.index) { it.copy(origin = it.destination, destination = it.origin) }
            }
            is FlightSearchEvent.OnPerformSearch -> saveDraftAndNavigate()
            FlightSearchEvent.OnAddSegment -> {
                _uiState.update { it.copy(segments = it.segments + com.softserveacademy.core.domain.model.FlightSegment()) }
            }
            is FlightSearchEvent.OnRemoveSegment -> {
                _uiState.update { it.copy(segments = it.segments.filterIndexed { i, _ -> i != event.index }) }
            }
            is FlightSearchEvent.OnShowDatePicker -> _uiState.update { it.copy(showDatePicker = true) }
            is FlightSearchEvent.OnDismissDatePicker -> _uiState.update { it.copy(showDatePicker = false) }
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
        val firstSegment = currentState.segments.firstOrNull() ?: com.softserveacademy.core.domain.model.FlightSegment()

        val validationResult = validateFlightSearchUseCase.validate(
            origin = firstSegment.origin,
            destination = firstSegment.destination,
            startDate = firstSegment.dateMillis ?: currentState.bookingDetailsState.startDateMillis,
            adults = currentState.adults
        )

        when (validationResult) {
            is FlightValidationResult.Success -> {
                viewModelScope.launch {
                    val finalSegments = if (currentState.selectedFlightType != com.softserveacademy.core.domain.model.FlightType.MULTI_CITY) {
                        // For One Way / Round Trip, we sincronize the calendar date to first segment
                        listOf(firstSegment.copy(
                            dateMillis = currentState.bookingDetailsState.startDateMillis
                        ))
                    } else {
                        currentState.segments
                    }
                    val draft = FlightBookingDraft(
                        origin = firstSegment.origin.trim().uppercase(), // Para compatibilidad
                        destination = firstSegment.destination.trim().uppercase(),
                        segments = finalSegments,
                        startDateMillis = firstSegment.dateMillis ?: currentState.bookingDetailsState.startDateMillis,
                        endDateMillis = currentState.bookingDetailsState.endDateMillis,
                        adults = currentState.adults,
                        children = currentState.children,
                        infants = currentState.infants,
                        flightType = currentState.selectedFlightType,
                        cabinClass = currentState.selectedCabinClass,
                        returnDateMillis = currentState.bookingDetailsState.endDateMillis
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

    private fun searchAirports(query: String, isOrigin: Boolean, index: Int) {
        viewModelScope.launch {
            searchAirportsUseCase(query).collect { list ->
                _uiState.update {
                    it.copy(
                        originSuggestions = if (isOrigin) list else it.originSuggestions,
                        destinationSuggestions = if (!isOrigin) list else it.destinationSuggestions,
                        activeSegmentIndex = index // Guardamos qué vuelo estamos editando
                    )
                }
            }
        }
    }

    private fun updateSegment(index: Int, block: (com.softserveacademy.core.domain.model.FlightSegment) -> com.softserveacademy.core.domain.model.FlightSegment) {
        _uiState.update { current ->
            val newList = current.segments.toMutableList()
            if (index in newList.indices) {
                newList[index] = block(newList[index])
            }
            current.copy(segments = newList, activeSegmentIndex = index)
        }
    }
}
