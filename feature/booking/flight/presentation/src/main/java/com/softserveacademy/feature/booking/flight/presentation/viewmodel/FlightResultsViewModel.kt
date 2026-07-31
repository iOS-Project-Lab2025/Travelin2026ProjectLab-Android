package com.softserveacademy.feature.booking.flight.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.PassengerType
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingDraftRepository
import com.softserveacademy.feature.booking.flight.domain.usecase.SearchFlightsUseCase
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

    /**
     * Exposes the current UI state to the Results screen.
     */
    val uiState: StateFlow<FlightResultsState> = _uiState.asStateFlow()



    init { loadDraftAndSearch() }

    /**
     * Loads the booking criteria from the local draft and triggers the remote search.
     * Ensures the UI is consistent with the user's previous selection.
     */
    private fun loadDraftAndSearch() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 1. Wait for the first valid draft from the DataStore
            val draft = draftRepository.getDraft()
                .filterNotNull()
                .first()

            // 2. Update state with criteria to maintain visual context
            _uiState.update { it.copy(
                origin = draft.origin,
                destination = draft.destination,
                totalPassengers = draft.adults + draft.children + draft.infants
            )}

            // 3. Map passenger counts to the Domain/GDS standard (ADU, CHD, INF)
            val passengers = mapOf(
                PassengerType.ADU to draft.adults,
                PassengerType.CHD to draft.children,
                PassengerType.INF to draft.infants
            )

            // 4. Call the search service
            searchFlightsUseCase(draft.origin, draft.destination, passengers)
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Network error") }
                }
                .collect { results ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        offers = results,
                        // Simulate that there are 20 flights in total for the "Show more" logic
                        totalAvailableCount = 20
                    )}
                }
        }
    }
}