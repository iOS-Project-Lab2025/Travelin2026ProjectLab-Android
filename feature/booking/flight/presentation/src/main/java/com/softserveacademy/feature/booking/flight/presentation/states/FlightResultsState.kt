package com.softserveacademy.feature.booking.flight.presentation.states


import com.softserveacademy.core.domain.model.FlightOffer

/**
 * UI State specific for the Results screen.
 */
data class FlightResultsState(
    val isLoading: Boolean = false,
    val allAvailableOffers: List<FlightOffer> = emptyList(),
    val visibleOffers: List<FlightOffer> = emptyList(),
    val error: Int? = null,
    val origin: String = "",
    val destination: String = "",
    val totalPassengers: Int = 0,
    val totalAvailableCount: Int = 0,
    val currentSegmentIndex: Int = 0,
    val totalSegments: Int = 0,
    val selectedOfferId: String? = null,
    val currencyCode: String = "USD",
    val exchangeRate: Double = 1.0

)