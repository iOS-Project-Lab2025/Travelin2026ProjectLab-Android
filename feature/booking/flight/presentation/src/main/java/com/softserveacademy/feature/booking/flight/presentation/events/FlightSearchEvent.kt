package com.softserveacademy.feature.booking.flight.presentation.events
import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.domain.model.CabinClass
import com.softserveacademy.core.domain.model.FlightType
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent

/**
 * User intents for the Flight Search screen.
 */
sealed interface FlightSearchEvent {

    data class OnOriginQueryChanged(val index: Int, val query: String) : FlightSearchEvent
    data class OnDestinationQueryChanged(val index: Int, val query: String) : FlightSearchEvent
    data class OnOriginSelected(val index: Int, val airport: Airport) : FlightSearchEvent
    data class OnDestinationSelected(val index: Int, val airport: Airport) : FlightSearchEvent
    data class OnDateSelected(val index: Int, val dateMillis: Long?) : FlightSearchEvent
    data class OnRemoveSegment(val index: Int) : FlightSearchEvent
    data class OnSwapSegmentLocations(val index: Int) : FlightSearchEvent
    data class OnAdultsChanged(val count: Int) : FlightSearchEvent
    data class OnChildrenChanged(val count: Int) : FlightSearchEvent
    data class OnInfantsChanged(val count: Int) : FlightSearchEvent
    data class InternalBookingEvent(val event: TravelEnterBookingDetailsEvent) : FlightSearchEvent
    data class OnFlightTypeSelected(val flightType: FlightType) : FlightSearchEvent
    data class OnCabinClassSelected(val cabinClass: CabinClass) : FlightSearchEvent
    object OnPerformSearch : FlightSearchEvent
    object OnShowPassengerSheet : FlightSearchEvent
    object OnShowCabinSheet : FlightSearchEvent
    object OnDismissCabinSheet : FlightSearchEvent
    object OnAddSegment : FlightSearchEvent
    object OnShowDatePicker : FlightSearchEvent
    object OnDismissDatePicker : FlightSearchEvent

}
