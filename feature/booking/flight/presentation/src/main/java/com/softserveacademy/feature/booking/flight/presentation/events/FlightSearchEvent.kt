package com.softserveacademy.feature.booking.flight.presentation.events
import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.domain.model.CabinClass
import com.softserveacademy.core.domain.model.FlightType
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
    data class OnFlightTypeSelected(val flightType: FlightType) : FlightSearchEvent
    data class OnCabinClassSelected(val cabinClass: CabinClass) : FlightSearchEvent
    object OnPerformSearch : FlightSearchEvent
    object OnShowPassengerSheet : FlightSearchEvent
    object OnSwapLocations : FlightSearchEvent
    object OnShowCabinSheet : FlightSearchEvent
    object OnDismissCabinSheet : FlightSearchEvent

}
