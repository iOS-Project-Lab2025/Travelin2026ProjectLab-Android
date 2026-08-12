package com.softserveacademy.feature.booking.flight.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.FlightType
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent
import com.softserveacademy.feature.booking.flight.domain.model.FlightBookingDraft
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingDraftRepository
import com.softserveacademy.feature.booking.flight.domain.usecase.SearchAirportsUseCase
import com.softserveacademy.feature.booking.flight.domain.usecase.ValidateFlightSearchUseCase
import com.softserveacademy.feature.booking.flight.presentation.R
import com.softserveacademy.feature.booking.flight.presentation.events.FlightSearchEvent
import com.softserveacademy.feature.booking.flight.presentation.states.FlightSearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Flight Search criteria screen.
 * Acts as an orchestrator for airport suggestions, business validation, and draft persistence.
 *
 * Key responsibilities:
 * 1. Manage dynamic flight segments (up to 4 for Multi-city).
 * 2. Persist user input across flight type changes.
 * 3. Generate secondary segments for Round Trip automatically.
 * 4. Translate technical exceptions into UI error messages.
 */
@HiltViewModel
class FlightSearchViewModel @Inject constructor(
    private val searchAirportsUseCase: SearchAirportsUseCase,
    private val validateFlightSearchUseCase: ValidateFlightSearchUseCase,
    private val draftRepository: FlightBookingDraftRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlightSearchState())
    val uiState: StateFlow<FlightSearchState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    /**
     * Primary entry point for all UI intents.
     * Dispatches logic based on the specific [FlightSearchEvent] received.
     */
    fun onEvent(event: FlightSearchEvent) {
        when (event) {
            is FlightSearchEvent.OnOriginQueryChanged -> {
                updateSegment(event.index) { it.copy(origin = event.query) }
                searchAirports(event.query, isOrigin = true, index = event.index)
                clearSegmentError(event.index, isOrigin = true)
            }

            is FlightSearchEvent.OnDestinationQueryChanged -> {
                updateSegment(event.index) { it.copy(destination = event.query) }
                searchAirports(event.query, isOrigin = false, index = event.index)
                clearSegmentError(event.index, isOrigin = false)
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
                handleDateSync(event.index, event.dateMillis)
            }

            is FlightSearchEvent.OnFlightTypeSelected -> handleFlightTypeChange(event.flightType)

            is FlightSearchEvent.OnSwapSegmentLocations -> {
                updateSegment(event.index) { it.copy(origin = it.destination, destination = it.origin) }
            }

            is FlightSearchEvent.OnAddSegment -> {
                _uiState.update { current ->
                    if (current.segments.size >= 4) return@update current
                    current.copy(segments = current.segments + com.softserveacademy.core.domain.model.FlightSegment())
                }
            }

            is FlightSearchEvent.OnRemoveSegment -> {
                _uiState.update { it.copy(segments = it.segments.filterIndexed { i, _ -> i != event.index }) }
            }

            is FlightSearchEvent.OnPerformSearch -> saveDraftAndNavigate()

            // Pass-through events for common booking sheets
            is FlightSearchEvent.OnAdultsChanged -> _uiState.update { it.copy(adults = event.count) }
            is FlightSearchEvent.OnChildrenChanged -> _uiState.update { it.copy(children = event.count) }
            is FlightSearchEvent.OnInfantsChanged -> _uiState.update { it.copy(infants = event.count) }
            is FlightSearchEvent.OnCabinClassSelected -> _uiState.update { it.copy(selectedCabinClass = event.cabinClass, showCabinSheet = false) }
            is FlightSearchEvent.OnShowCabinSheet -> _uiState.update { it.copy(showCabinSheet = true) }
            is FlightSearchEvent.OnDismissCabinSheet -> _uiState.update { it.copy(showCabinSheet = false) }
            is FlightSearchEvent.OnShowPassengerSheet -> _uiState.update { it.copy(bookingDetailsState = it.bookingDetailsState.copy(showGuestBottomSheet = true)) }
            is FlightSearchEvent.OnShowDatePicker -> _uiState.update { it.copy(showDatePicker = true) }
            is FlightSearchEvent.OnDismissDatePicker -> _uiState.update { it.copy(showDatePicker = false) }
            is FlightSearchEvent.InternalBookingEvent -> handleInternalEvent(event.event)
        }
    }

    /**
     * Logic to sanitize, validate, and persist the booking criteria.
     * In Round Trip mode, it automatically generates the return segment.
     */
    private fun saveDraftAndNavigate() {
        val currentState = _uiState.value
        val sanitizedSegments = currentState.segments.map {
            it.copy(origin = it.origin.trim().uppercase(), destination = it.destination.trim().uppercase())
        }

        val result = validateFlightSearchUseCase.validate(
            segments = sanitizedSegments,
            isRoundTrip = currentState.selectedFlightType == FlightType.ROUND_TRIP,
            endDate = currentState.bookingDetailsState.endDateMillis
        )

        if (result.isValid) {
            viewModelScope.launch {
                val finalSegments = when (currentState.selectedFlightType) {
                    FlightType.ROUND_TRIP -> listOf(
                        sanitizedSegments[0].copy(dateMillis = currentState.bookingDetailsState.startDateMillis),
                        sanitizedSegments[0].copy(
                            origin = sanitizedSegments[0].destination,
                            destination = sanitizedSegments[0].origin,
                            dateMillis = currentState.bookingDetailsState.endDateMillis
                        )
                    )
                    FlightType.ONE_WAY -> listOf(sanitizedSegments[0].copy(dateMillis = currentState.bookingDetailsState.startDateMillis))
                    else -> sanitizedSegments
                }

                val draft = FlightBookingDraft(
                    segments = finalSegments,
                    startDateMillis = currentState.bookingDetailsState.startDateMillis,
                    endDateMillis = currentState.bookingDetailsState.endDateMillis,
                    adults = currentState.adults,
                    children = currentState.children,
                    infants = currentState.infants,
                    flightType = currentState.selectedFlightType,
                    cabinClass = currentState.selectedCabinClass
                )
                draftRepository.saveDraft(draft)
                _navigationEvent.emit(Unit)
            }
        } else {
            _uiState.update { it.copy(errors = result.segmentErrors, globalDateError = result.globalDateError) }
        }
    }

    /**
     * Fetches airport suggestions and handles network-related errors.
     */
    private fun searchAirports(query: String, isOrigin: Boolean, index: Int) {
        if (query.length < 3) return
        viewModelScope.launch {
            searchAirportsUseCase(query)
                .catch { e ->
                    val errorRes = if (e is java.io.IOException) R.string.flight_error_network else R.string.flight_error_occurred
                    _uiState.update { it.copy(errorMessage = errorRes) }
                }
                .collect { list ->
                    _uiState.update { it.copy(
                        originSuggestions = if (isOrigin) list else emptyList(),
                        destinationSuggestions = if (!isOrigin) list else emptyList(),
                        activeSegmentIndex = index,
                        errorMessage = null
                    )}
                }
        }
    }

    private fun handleFlightTypeChange(type: FlightType) {
        _uiState.update { current ->
            val updatedSegments = if (type == FlightType.MULTI_CITY && current.segments.size < 2) {
                current.segments + com.softserveacademy.core.domain.model.FlightSegment()
            } else current.segments

            current.copy(selectedFlightType = type, segments = updatedSegments)
        }
    }

    private fun handleDateSync(index: Int, dateMillis: Long?) {
        _uiState.update { current ->
            val newErrors = current.errors.toMutableMap().apply { remove(index) }
            current.copy(
                errors = newErrors,
                globalDateError = null,
                bookingDetailsState = if (index == 0) current.bookingDetailsState.copy(startDateMillis = dateMillis) else current.bookingDetailsState
            )
        }
    }

    private fun clearSegmentError(index: Int, isOrigin: Boolean) {
        _uiState.update { current ->
            val newErrors = current.errors.toMutableMap()
            val segmentError = newErrors[index]
            if (segmentError != null) {
                newErrors[index] = if (isOrigin) segmentError.copy(originError = null) else segmentError.copy(destinationError = null)
            }
            current.copy(errors = newErrors)
        }
    }

    private fun updateSegment(index: Int, block: (com.softserveacademy.core.domain.model.FlightSegment) -> com.softserveacademy.core.domain.model.FlightSegment) {
        _uiState.update { current ->
            val newList = current.segments.toMutableList()
            if (index in newList.indices) newList[index] = block(newList[index])
            current.copy(segments = newList, activeSegmentIndex = index)
        }
    }

    private fun handleInternalEvent(event: TravelEnterBookingDetailsEvent) {
        _uiState.update { current ->
            when (event) {
                is TravelEnterBookingDetailsEvent.OnDateRangeSelected -> {
                    val updatedSegments = current.segments.toMutableList()
                    if (updatedSegments.isNotEmpty()) updatedSegments[0] = updatedSegments[0].copy(dateMillis = event.startDateMillis)
                    current.copy(bookingDetailsState = current.bookingDetailsState.copy(startDateMillis = event.startDateMillis, endDateMillis = event.endDateMillis), segments = updatedSegments)
                }
                TravelEnterBookingDetailsEvent.OnAcceptClick, TravelEnterBookingDetailsEvent.OnDismissBottomSheet ->
                    current.copy(bookingDetailsState = current.bookingDetailsState.copy(showGuestBottomSheet = false))
                else -> current
            }
        }
    }
}