package com.softserveacademy.feature.booking.flight.presentation.states

import com.softserveacademy.core.domain.model.FlightOffer

/**
 * UI State for the Flight Results list screen.
 * Manages the iterative process of selecting multiple segments.
 *
 * @property isLoading Whether a search request is currently in progress.
 * @property allAvailableOffers The full list of flights returned by the server.
 * @property visibleOffers Subset of offers currently rendered (for pagination).
 * @property error Resource ID for technical errors (e.g., Network, Server).
 * @property currentSegmentIndex The 0-based index of the flight being selected (e.g., Outbound is 0).
 * @property totalSegments Total number of flights to be selected in this booking.
 * @property selectedOfferId ID of the picked flight in the current list for UI highlighting.
 * @property currencyCode The currency to display for all prices (default "USD").
 * @property exchangeRate Rate used to convert base prices into the displayed currency.
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