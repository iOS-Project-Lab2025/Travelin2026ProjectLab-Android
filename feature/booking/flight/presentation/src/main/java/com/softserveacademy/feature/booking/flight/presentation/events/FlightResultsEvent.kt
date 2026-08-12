package com.softserveacademy.feature.booking.flight.presentation.events

/**
 * User intents for the Flight Results list screen.
 * Handles selection, pagination, and iterative navigation.
 */
sealed interface FlightResultsEvent {
    /** Triggered when the user attempts to re-fetch flights after a failure. */
    object OnRetryClick : FlightResultsEvent

    /** Triggered to fetch the next set of matching flights. */
    object OnLoadMore : FlightResultsEvent

    /** Navigates back to the previous segment or search criteria. */
    object OnBackClick : FlightResultsEvent

    /** Confirms the currently selected flight and moves to the next step. */
    object OnNextClick : FlightResultsEvent

    /** Visual selection of a flight from the list. */
    data class OnFlightSelected(val flightId: String) : FlightResultsEvent
}