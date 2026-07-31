package com.softserveacademy.feature.booking.flight.presentation.events
import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent

/**
 * User intents for the Flight Search screen.
 */
sealed interface FlightSearchEvent {

    data class OnOriginQueryChanged(val query: String) : FlightSearchEvent
    data class OnDestinationQueryChanged(val query: String) : FlightSearchEvent
    data class OnOriginSelected(val airport: Airport) : FlightSearchEvent
    data class OnDestinationSelected(val airport: Airport) : FlightSearchEvent
    data class OnAdultsChanged(val count: Int) : FlightSearchEvent
    data class OnChildrenChanged(val count: Int) : FlightSearchEvent
    data class OnInfantsChanged(val count: Int) : FlightSearchEvent
    data class InternalBookingEvent(val event: TravelEnterBookingDetailsEvent) : FlightSearchEvent
    object OnPerformSearch : FlightSearchEvent
    object OnShowPassengerSheet : FlightSearchEvent

}
