package com.softserveacademy.feature.booking.flight.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent
import com.softserveacademy.feature.booking.flight.domain.model.FlightBookingDraft
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingDraftRepository
import com.softserveacademy.feature.booking.flight.domain.usecase.SearchAirportsUseCase
import com.softserveacademy.feature.booking.flight.domain.usecase.ValidateFlightSearchUseCase
import com.softserveacademy.feature.booking.flight.presentation.events.FlightSearchEvent
import com.softserveacademy.feature.booking.flight.presentation.states.FlightSearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for flight searching criteria.
 * Acts as an orchestrator for segments, business validation, and draft persistence.
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
     * Handles all user intents for the search screen.
     */
    fun onEvent(event: FlightSearchEvent) {
        when (event) {
            is FlightSearchEvent.OnOriginQueryChanged -> {
                updateSegment(event.index) { it.copy(origin = event.query) }
                searchAirports(event.query, isOrigin = true, index = event.index)

                // clean the error field if it has the correct information
                _uiState.update { current ->
                    val newErrors = current.errors.toMutableMap()
                    val segmentError = newErrors[event.index]
                    if (segmentError != null) {
                        newErrors[event.index] = segmentError.copy(originError = null)
                    }
                    current.copy(errors = newErrors)
                }
            }

            is FlightSearchEvent.OnDestinationQueryChanged -> {
                updateSegment(event.index) { it.copy(destination = event.query) }
                searchAirports(event.query, isOrigin = false, index = event.index)

                // --- clean the field if it has the correct information ---
                _uiState.update { current ->
                    val newErrors = current.errors.toMutableMap()
                    val segmentError = newErrors[event.index]
                    if (segmentError != null) {
                        newErrors[event.index] = segmentError.copy(destinationError = null)
                    }
                    current.copy(errors = newErrors)
                }
            }

            is FlightSearchEvent.OnOriginSelected -> {
                updateSegment(event.index) { it.copy(origin = event.airport.code) }
                _uiState.update { current ->
                    val newErrors = current.errors.toMutableMap()
                    newErrors[event.index] = newErrors[event.index]?.copy(originError = null) ?: ValidateFlightSearchUseCase.SegmentError()
                    current.copy(originSuggestions = emptyList(), errors = newErrors)
                }
            }

            is FlightSearchEvent.OnDestinationSelected -> {
                updateSegment(event.index) { it.copy(destination = event.airport.code) }
                _uiState.update { current ->
                    val newErrors = current.errors.toMutableMap()
                    newErrors[event.index] = newErrors[event.index]?.copy(destinationError = null) ?: ValidateFlightSearchUseCase.SegmentError()
                    current.copy(destinationSuggestions = emptyList(), errors = newErrors)
                }
            }

            is FlightSearchEvent.OnDateSelected -> {
                updateSegment(event.index) { it.copy(dateMillis = event.dateMillis) }

                _uiState.update { current ->
                    // 1. Convertimos el mapa de errores a mutable para editarlo
                    val newErrors = current.errors.toMutableMap()

                    // 2. Limpiamos el error específico del campo que acabamos de tocar
                    newErrors.remove(event.index)

                    // 3. LIMPIEZA DE DEPENDENCIAS:
                    // Si cambió una fecha, los errores de "Secuencia" de otros vuelos podrían ya no aplicar.
                    // Recorremos el mapa y quitamos cualquier error de secuencia previo.
                    current.errors.forEach { (idx, error) ->
                        if (error.dateError == ValidateFlightSearchUseCase.FlightError.INVALID_DATE_SEQUENCE) {
                            newErrors[idx] = error.copy(dateError = null)
                        }
                    }

                    current.copy(
                        errors = newErrors,
                        globalDateError = null, // También limpiamos el error global (Round Trip)
                        bookingDetailsState = if (event.index == 0)
                            current.bookingDetailsState.copy(startDateMillis = event.dateMillis)
                        else current.bookingDetailsState
                    )
                }
            }

            is FlightSearchEvent.OnFlightTypeSelected -> {
                _uiState.update { current ->
                    // 1. Rescatamos la fecha actual
                    val firstDate = current.segments.getOrNull(0)?.dateMillis ?: current.bookingDetailsState.startDateMillis
                    val hasEndDate = current.bookingDetailsState.endDateMillis != null

                    // 2. Calculamos el nuevo error de fecha basado en el modo al que vamos
                    val newGlobalError = if (current.globalDateError != null || current.errors[0]?.dateError != null) {
                        when (event.flightType) {
                            com.softserveacademy.core.domain.model.FlightType.ROUND_TRIP -> {
                                if (firstDate == null || !hasEndDate) ValidateFlightSearchUseCase.FlightError.MISSING_RETURN_DATE else null
                            }
                            else -> {
                                if (firstDate == null) ValidateFlightSearchUseCase.FlightError.INVALID_DATE else null
                            }
                        }
                    } else null

                    current.copy(
                        selectedFlightType = event.flightType,
                        globalDateError = newGlobalError,
                        segments = if (event.flightType == com.softserveacademy.core.domain.model.FlightType.MULTI_CITY && current.segments.size < 2)
                            listOf(current.segments[0].copy(dateMillis = firstDate), com.softserveacademy.core.domain.model.FlightSegment())
                        else listOf(current.segments[0].copy(dateMillis = firstDate))
                    )
                }
            }

            is FlightSearchEvent.OnSwapSegmentLocations -> {
                updateSegment(event.index) {
                    it.copy(
                        origin = it.destination,
                        destination = it.origin
                    )
                }
            }

            is FlightSearchEvent.OnAddSegment -> {
                _uiState.update { it.copy(segments = it.segments + com.softserveacademy.core.domain.model.FlightSegment()) }
            }

            is FlightSearchEvent.OnRemoveSegment -> {
                _uiState.update { it.copy(segments = it.segments.filterIndexed { i, _ -> i != event.index }) }
            }

            is FlightSearchEvent.OnAdultsChanged -> _uiState.update { it.copy(adults = event.count) }
            is FlightSearchEvent.OnChildrenChanged -> _uiState.update { it.copy(children = event.count) }
            is FlightSearchEvent.OnInfantsChanged -> _uiState.update { it.copy(infants = event.count) }
            is FlightSearchEvent.OnCabinClassSelected -> {
                _uiState.update {
                    it.copy(
                        selectedCabinClass = event.cabinClass,
                        showCabinSheet = false
                    )
                }
            }

            is FlightSearchEvent.OnShowCabinSheet -> _uiState.update { it.copy(showCabinSheet = true) }
            is FlightSearchEvent.OnDismissCabinSheet -> _uiState.update { it.copy(showCabinSheet = false) }
            is FlightSearchEvent.OnShowPassengerSheet -> _uiState.update {
                it.copy(
                    bookingDetailsState = it.bookingDetailsState.copy(showGuestBottomSheet = true)
                )
            }

            is FlightSearchEvent.OnShowDatePicker -> _uiState.update { it.copy(showDatePicker = true) }
            is FlightSearchEvent.OnDismissDatePicker -> _uiState.update { it.copy(showDatePicker = false) }
            is FlightSearchEvent.InternalBookingEvent -> handleInternalEvent(event.event)
            FlightSearchEvent.OnPerformSearch -> saveDraftAndNavigate()

        }
    }

    private fun handleInternalEvent(event: TravelEnterBookingDetailsEvent) {
        _uiState.update { current ->
            val newState = when (event) {
                is TravelEnterBookingDetailsEvent.OnDateRangeSelected -> {
                    // Update segments with the new start date
                    val updatedSegments = current.segments.toMutableList()
                    if (updatedSegments.isNotEmpty()) {
                        updatedSegments[0] =
                            updatedSegments[0].copy(dateMillis = event.startDateMillis)
                    }
                    current.bookingDetailsState.copy(
                        startDateMillis = event.startDateMillis,
                        endDateMillis = event.endDateMillis
                    ).let {
                        return@let current.copy(
                            bookingDetailsState = it,
                            segments = updatedSegments
                        )
                    }
                }

                TravelEnterBookingDetailsEvent.OnAcceptClick, TravelEnterBookingDetailsEvent.OnDismissBottomSheet ->
                    current.bookingDetailsState.copy(showGuestBottomSheet = false)
                        .let { return@let current.copy(bookingDetailsState = it) }

                else -> current.bookingDetailsState.let {
                    return@let current.copy(
                        bookingDetailsState = it
                    )
                }
            }
            newState
        }
    }

    /**
     * Logic to sanitize, validate, and persist the booking criteria.
     */
    private fun saveDraftAndNavigate() {
        val currentState = _uiState.value

        // 1. SECURITY: Sanitize inputs before validation
        val sanitizedSegments = currentState.segments.map {
            it.copy(
                origin = it.origin.trim().uppercase(),
                destination = it.destination.trim().uppercase()
            )
        }

        // 2. RUN VALIDATION
        val result = validateFlightSearchUseCase.validate(
            segments = sanitizedSegments,
            isRoundTrip = currentState.selectedFlightType == com.softserveacademy.core.domain.model.FlightType.ROUND_TRIP,
            endDate = currentState.bookingDetailsState.endDateMillis
        )

        if (result.isValid) {
            viewModelScope.launch {
                _uiState.update { it.copy(errors = emptyMap(), globalDateError = null) }
                // Sincronizar fechas para el primer tramo si es Round/One Way
                val finalSegments =
                    if (currentState.selectedFlightType != com.softserveacademy.core.domain.model.FlightType.MULTI_CITY) {
                        listOf(sanitizedSegments[0].copy(dateMillis = currentState.bookingDetailsState.startDateMillis))
                    } else {
                        sanitizedSegments
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
            _uiState.update { currentState ->
                // Guardamos los errores tal cual vienen del UseCase (con Enums)
                currentState.copy(
                    errors = result.segmentErrors,
                    globalDateError = result.globalDateError,
                    errorMessage = null
                )
            }
        }

    }

    private fun searchAirports(query: String, isOrigin: Boolean, index: Int) {
        viewModelScope.launch {
            searchAirportsUseCase(query).collect { list ->
                _uiState.update { current ->
                    current.copy(
                        originSuggestions = if (isOrigin) list else emptyList(),
                        destinationSuggestions = if (!isOrigin) list else emptyList(),
                        activeSegmentIndex = index
                    )
                }
            }
        }
    }

    private fun updateSegment(
        index: Int,
        block: (com.softserveacademy.core.domain.model.FlightSegment) -> com.softserveacademy.core.domain.model.FlightSegment
    ) {
        _uiState.update { current ->
            val newList = current.segments.toMutableList()
            if (index in newList.indices) {
                newList[index] = block(newList[index])
            }
            current.copy(segments = newList, activeSegmentIndex = index)
        }
    }
}