package com.softserveacademy.feature.booking.tour.presentation.events

import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent

/**
 * Sealed interface representing UI events specific to the tour enter booking details screen.
 */
sealed interface TourEnterBookingDetailsEvent {
    data class OnAdultsCountChange(val count: Int) : TourEnterBookingDetailsEvent
    data class OnChildrenCountChange(val count: Int) : TourEnterBookingDetailsEvent
    data class OnInfantsCountChange(val count: Int) : TourEnterBookingDetailsEvent
    
    /**
     * Wrapper for common enter booking details screen events.
     */
    data class ScreenEvent(val event: TravelEnterBookingDetailsEvent) : TourEnterBookingDetailsEvent
}
