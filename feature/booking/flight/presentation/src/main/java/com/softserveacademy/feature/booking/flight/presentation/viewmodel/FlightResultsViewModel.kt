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
 * Orchestrates search results fetching and manages the iterative segment selection process.
 *
 * Flow:
 * 1. Loads current segment criteria from [FlightBookingDraft].
 * 2. Fetches matching flights for that specific segment.
 * 3. Handles selection and advances the [currentSelectingIndex] until all segments are picked.
 */
@HiltViewModel
class FlightResultsViewModel @Inject constructor(
    private val draftRepository: FlightBookingDraftRepository,
    private val searchFlightsUseCase: SearchFlightsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlightResultsState())
    val uiState: StateFlow<FlightResultsState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Unit>()
    /** Dispatched when all segments in the draft have a selected offer. */
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _backNavigationEvent = MutableSharedFlow<Unit>()
    /** Dispatched when user exits the results flow (e.g., clicking back on the first segment). */
    val backNavigationEvent = _backNavigationEvent.asSharedFlow()

    init {
        loadDraftAndSearch()
    }

    /**
     * Entry point for user actions on the results screen.
     */
    fun onEvent(event: FlightResultsEvent) {
        when (event) {
            is FlightResultsEvent.OnRetryClick -> loadDraftAndSearch()

            is FlightResultsEvent.OnFlightSelected -> {
                // Highlighting only: does not advance until 'Next' is clicked
                _uiState.update { it.copy(selectedOfferId = event.flightId) }
            }

            is FlightResultsEvent.OnNextClick -> handleNextStep()

            is FlightResultsEvent.OnBackClick -> handleBackStep()

            is FlightResultsEvent.OnLoadMore -> {
                _uiState.update { current ->
                    val nextCount = current.visibleOffers.size + 5
                    current.copy(visibleOffers = current.allAvailableOffers.take(nextCount))
                }
            }
        }
    }

    private fun handleNextStep() {
        viewModelScope.launch {
            val currentOfferId = _uiState.value.selectedOfferId ?: return@launch
            val draft = draftRepository.getDraft().filterNotNull().first()
            val selectedOffer = _uiState.value.allAvailableOffers.find { it.id == currentOfferId }

            selectedOffer?.let { offer ->
                val isLastStep = draft.currentSelectingIndex == draft.segments.size - 1
                val updatedDraft = draft.copy(
                    selectedOffers = draft.selectedOffers + (draft.currentSelectingIndex to offer),
                    currentSelectingIndex = if (isLastStep) draft.currentSelectingIndex else draft.currentSelectingIndex + 1
                )
                draftRepository.saveDraft(updatedDraft)

                if (isLastStep) {
                    _navigationEvent.emit(Unit)
                } else {
                    // Reset selection for the next segment and reload
                    _uiState.update { it.copy(selectedOfferId = null) }
                    loadDraftAndSearch()
                }
            }
        }
    }

    private fun handleBackStep() {
        viewModelScope.launch {
            val draft = draftRepository.getDraft().filterNotNull().first()
            if (draft.currentSelectingIndex > 0) {
                val updatedDraft = draft.copy(currentSelectingIndex = draft.currentSelectingIndex - 1)
                draftRepository.saveDraft(updatedDraft)
                loadDraftAndSearch()
            } else {
                _backNavigationEvent.emit(Unit)
            }
        }
    }

    /**
     * Loads the criteria for the active segment from the draft and triggers search.
     */
    private fun loadDraftAndSearch() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val draft = draftRepository.getDraft().filterNotNull().first()
            val currentSegment = draft.segments.getOrNull(draft.currentSelectingIndex) ?: draft.segments[0]

            // Check if there's already a selection for this segment to restore UI state
            val selectedId = draft.selectedOffers[draft.currentSelectingIndex]?.id

            _uiState.update { it.copy(
                origin = currentSegment.origin,
                destination = currentSegment.destination,
                totalPassengers = draft.adults + draft.children + draft.infants,
                selectedOfferId = selectedId,
                currentSegmentIndex = draft.currentSelectingIndex,
                totalSegments = draft.segments.size,
                currencyCode = "USD", // Logic ready for future multi-currency support
                exchangeRate = 1.0
            )}

            val passengers = mapOf(
                PassengerType.ADU to draft.adults,
                PassengerType.CHD to draft.children,
                PassengerType.INF to draft.infants
            )

            searchFlightsUseCase(
                origin = currentSegment.origin,
                destination = currentSegment.destination,
                passengerCounts = passengers,
                cabinClass = draft.cabinClass,
                departureDate = currentSegment.dateMillis,
                returnDate = null // In this flow, we search segment by segment
            )
                .catch { e ->
                    val errorRes = if (e is java.io.IOException) R.string.flight_error_network else R.string.flight_error_occurred
                    _uiState.update { it.copy(isLoading = false, error = errorRes) }
                }
                .collect { results ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        allAvailableOffers = results,
                        visibleOffers = results.take(5),
                        totalAvailableCount = results.size
                    )}
                }
        }
    }
}