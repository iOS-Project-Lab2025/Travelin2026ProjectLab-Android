package com.softserveacademy.feature.booking.flight.presentation.events

sealed interface FlightResultsEvent {
    object OnRetryClick : FlightResultsEvent
    object OnLoadMore : FlightResultsEvent
    object OnBackClick : FlightResultsEvent
    object OnNextClick : FlightResultsEvent
    data class OnFlightSelected(val flightId: String) : FlightResultsEvent
}