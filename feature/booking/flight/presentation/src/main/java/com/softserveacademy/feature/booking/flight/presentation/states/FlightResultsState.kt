package com.softserveacademy.feature.booking.flight.presentation.states

import com.softserveacademy.core.domain.model.FlightOffer

/**
 * UI State specific for the Results screen.
 */
data class FlightResultsState(
    val isLoading: Boolean = false,
    val offers: List<FlightOffer> = emptyList(),
    val error: String? = null,
    val origin: String = "",
    val destination: String = "",
    val totalPassengers: Int = 0,
    val totalAvailableCount: Int = 0
)