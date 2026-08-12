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
    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent = _navigationEvent.asSharedFlow()
    private val _backNavigationEvent = MutableSharedFlow<Unit>()
    val backNavigationEvent = _backNavigationEvent.asSharedFlow()


    init {
        loadDraftAndSearch()
    }

    fun onEvent(event: FlightResultsEvent) {
        when (event) {
            is FlightResultsEvent.OnRetryClick -> loadDraftAndSearch()


            is FlightResultsEvent.OnFlightSelected -> {
                _uiState.update { it.copy(selectedOfferId = event.flightId) }
            }
            is FlightResultsEvent.OnLoadMore -> {
                _uiState.update { current ->
                    val nextCount = current.visibleOffers.size + 5
                    current.copy(
                        visibleOffers = current.allAvailableOffers.take(nextCount),
                    )
                }
            }

            is FlightResultsEvent.OnBackClick -> {
                viewModelScope.launch {
                    val draft = draftRepository.getDraft().first()
                    draft?.let {
                        if (it.currentSelectingIndex > 0) {
                            // Retrocedemos el índice y recargamos
                            val updatedDraft = it.copy(currentSelectingIndex = it.currentSelectingIndex - 1)
                            draftRepository.saveDraft(updatedDraft)
                            loadDraftAndSearch()
                        } else {
                            // Si es el primer tramo, disparamos el evento para salir de la pantalla
                            // (puedes crear un SharedFlow nuevo o usar el navigationEvent)
                            _backNavigationEvent.emit(Unit)
                        }
                    }
                }
            }

            is FlightResultsEvent.OnNextClick -> {
                viewModelScope.launch {
                    val currentOfferId = _uiState.value.selectedOfferId
                    if (currentOfferId == null) return@launch // No hacemos nada si no hay selección

                    val currentDraft = draftRepository.getDraft().first()
                    currentDraft?.let { draft ->
                        val selectedOffer = _uiState.value.allAvailableOffers.find { it.id == currentOfferId }

                        selectedOffer?.let { offer ->
                            // Guardamos la oferta en el draft
                            val updatedOffers = draft.selectedOffers + (draft.currentSelectingIndex to offer)
                            val isLastStep = draft.currentSelectingIndex == draft.segments.size - 1

                            val updatedDraft = draft.copy(
                                selectedOffers = updatedOffers,
                                // Avanzamos el índice solo si no es el último paso
                                currentSelectingIndex = if (isLastStep) draft.currentSelectingIndex else draft.currentSelectingIndex + 1
                            )
                            draftRepository.saveDraft(updatedDraft)

                            if (isLastStep) {
                                _navigationEvent.emit(Unit) // Navegación final
                            } else {
                                loadDraftAndSearch() // Cargar siguiente segmento
                            }
                        }
                    }
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
            val currentSegment = draft.segments.getOrNull(draft.currentSelectingIndex) ?: draft.segments[0]

            val selectedId = draft.selectedOffers[draft.currentSelectingIndex]?.id

            _uiState.update { it.copy(
                origin = currentSegment.origin,
                destination = currentSegment.destination,
                totalPassengers = draft.adults + draft.children + draft.infants,
                selectedOfferId = selectedId,
                currentSegmentIndex = draft.currentSelectingIndex,
                totalSegments = draft.segments.size,
                currencyCode = "USD",
                exchangeRate = 1.0
            )}

            val passengers = mapOf(
                com.softserveacademy.core.domain.model.PassengerType.ADU to draft.adults,
                com.softserveacademy.core.domain.model.PassengerType.CHD to draft.children,
                com.softserveacademy.core.domain.model.PassengerType.INF to draft.infants
            )

            // 3. Trigger the search and handle errors
            searchFlightsUseCase(
                currentSegment.origin,
                currentSegment.destination,
                passengers,
                cabinClass = draft.cabinClass,
                departureDate = currentSegment.dateMillis,
                returnDate = null)
                .catch { e ->
                    // Handles network or server errors
                    val errorRes = if (e is java.io.IOException) R.string.flight_error_network
                    else R.string.flight_error_occurred
                    _uiState.update { it.copy(isLoading = false, error = errorRes) }

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