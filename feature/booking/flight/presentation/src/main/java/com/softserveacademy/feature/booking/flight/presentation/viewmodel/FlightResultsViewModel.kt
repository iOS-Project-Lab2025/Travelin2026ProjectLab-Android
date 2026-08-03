package com.softserveacademy.feature.booking.flight.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.PassengerType
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingDraftRepository
import com.softserveacademy.feature.booking.flight.domain.usecase.SearchFlightsUseCase
import com.softserveacademy.feature.booking.flight.presentation.R
import com.softserveacademy.feature.booking.flight.presentation.events.FlightResultsEvent
import com.softserveacademy.feature.booking.flight.presentation.states.FlightResultsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Flight Results screen.
 * Orchestrates the data flow between the persisted booking draft and the search API.
 */
@HiltViewModel
class FlightResultsViewModel @Inject constructor(
    private val draftRepository: FlightBookingDraftRepository,
    private val searchFlightsUseCase: SearchFlightsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlightResultsState())
    val uiState: StateFlow<FlightResultsState> = _uiState.asStateFlow()

    init {
        loadDraftAndSearch()
    }

    fun onEvent(event: FlightResultsEvent) {
        when (event) {
            is FlightResultsEvent.OnRetryClick -> loadDraftAndSearch()


            is FlightResultsEvent.OnFlightSelected -> {
                // Aquí actualizaremos el draft con el ID seleccionado
                viewModelScope.launch {
                    val currentDraft = draftRepository.getDraft().first()
                    currentDraft?.let {
                        draftRepository.saveDraft(it.copy(selectedFlightId = event.flightId))
                    }
                }
            }
            is FlightResultsEvent.OnLoadMore -> {
                _uiState.update { current ->
                    val nextCount = current.visibleOffers.size + 5
                    current.copy(
                        visibleOffers = current.allAvailableOffers.take(nextCount),
                    )
                }
            }
        }
    }

    /**
     * Public function to manually trigger a retry.
     */
    fun retrySearch() {
        loadDraftAndSearch()
    }

    private fun loadDraftAndSearch() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // 1. Get the draft from DataStore
            val draft = draftRepository.getDraft()
                .filterNotNull()
                .first()

            // 2. Extract criteria from the new segments structure
            val firstSegment = draft.segments.firstOrNull() ?: com.softserveacademy.core.domain.model.FlightSegment()

            _uiState.update { it.copy(
                origin = firstSegment.origin,
                destination = firstSegment.destination,
                totalPassengers = draft.adults + draft.children + draft.infants
            )}

            val passengers = mapOf(
                com.softserveacademy.core.domain.model.PassengerType.ADU to draft.adults,
                com.softserveacademy.core.domain.model.PassengerType.CHD to draft.children,
                com.softserveacademy.core.domain.model.PassengerType.INF to draft.infants
            )

            // 3. Trigger the search and handle errors
            searchFlightsUseCase(
                firstSegment.origin,
                firstSegment.destination,
                passengers,
                cabinClass = draft.cabinClass,
                departureDate = firstSegment.dateMillis,
                returnDate = draft.returnDateMillis)
                .catch { e ->
                    // Handles network or server errors
                    _uiState.update { it.copy(isLoading = false, error = R.string.flight_error_network, allAvailableOffers = emptyList()) }
                }
                .collect { results ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        allAvailableOffers = results,
                        visibleOffers = results.take(5), //how many results we show
                        error = null,
                        totalAvailableCount = results.size
                    )}
                }
        }
    }
}