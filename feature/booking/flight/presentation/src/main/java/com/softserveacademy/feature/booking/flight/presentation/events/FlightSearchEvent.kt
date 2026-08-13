package com.softserveacademy.feature.booking.flight.presentation.events

import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.domain.model.CabinClass
import com.softserveacademy.core.domain.model.FlightType
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent

/**
 * User intents for the Flight Search criteria screen.
 * Captures all interactions from text inputs to preference selections.
 */
sealed interface FlightSearchEvent {

    /** Triggered when the user types in the Origin field. */
    data class OnOriginQueryChanged(val index: Int, val query: String) : FlightSearchEvent

    /** Triggered when the user types in the Destination field. */
    data class OnDestinationQueryChanged(val index: Int, val query: String) : FlightSearchEvent

    /** Triggered when an airport is selected from the suggestions list. */
    data class OnOriginSelected(val index: Int, val airport: Airport) : FlightSearchEvent

    /** Triggered when an airport is selected from the suggestions list. */
    data class OnDestinationSelected(val index: Int, val airport: Airport) : FlightSearchEvent

    /** Triggered when a specific date is chosen for a flight segment. */
    data class OnDateSelected(val index: Int, val dateMillis: Long?) : FlightSearchEvent

    /** Triggered in Multi-city mode to remove a specific tranche. */
    data class OnRemoveSegment(val index: Int) : FlightSearchEvent

    /** Switches Origin and Destination for a specific segment. */
    data class OnSwapSegmentLocations(val index: Int) : FlightSearchEvent

    // Passenger count changes
    data class OnAdultsChanged(val count: Int) : FlightSearchEvent
    data class OnChildrenChanged(val count: Int) : FlightSearchEvent
    data class OnInfantsChanged(val count: Int) : FlightSearchEvent

    /** Maps events from the common booking UI sheets. */
    data class InternalBookingEvent(val event: TravelEnterBookingDetailsEvent) : FlightSearchEvent

    /** Switches between Round-trip, One-way, and Multi-city. */
    data class OnFlightTypeSelected(val flightType: FlightType) : FlightSearchEvent

    /** Changes the travel preference class. */
    data class OnCabinClassSelected(val cabinClass: CabinClass) : FlightSearchEvent

    /** Validates inputs and prepares to navigate to results. */
    object OnPerformSearch : FlightSearchEvent

    // UI visibility triggers
    object OnShowPassengerSheet : FlightSearchEvent
    object OnShowCabinSheet : FlightSearchEvent
    object OnDismissCabinSheet : FlightSearchEvent

    /** Adds a new empty segment in Multi-city mode. */
    object OnAddSegment : FlightSearchEvent

    object OnShowDatePicker : FlightSearchEvent
    object OnDismissDatePicker : FlightSearchEvent
}
