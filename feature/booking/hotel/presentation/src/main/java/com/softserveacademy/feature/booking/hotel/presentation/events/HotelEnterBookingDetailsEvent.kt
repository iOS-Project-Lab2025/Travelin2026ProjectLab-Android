package com.softserveacademy.feature.booking.hotel.presentation.events

import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent

/**
 * Sealed interface representing UI events specific to the hotel enter booking details screen.
 */
sealed interface HotelEnterBookingDetailsEvent {
    data class OnAdultsCountChange(val count: Int) : HotelEnterBookingDetailsEvent
    data class OnChildrenCountChange(val count: Int) : HotelEnterBookingDetailsEvent
    data class OnHasPetsChange(val hasPets: Boolean) : HotelEnterBookingDetailsEvent
    
    /**
     * Wrapper for common enter booking details screen events.
     */
    data class ScreenEvent(val event: TravelEnterBookingDetailsEvent) : HotelEnterBookingDetailsEvent
}
