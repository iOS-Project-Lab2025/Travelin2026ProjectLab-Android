package com.softserveacademy.feature.booking.flight.presentation.states

import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.feature.booking.common.presentation.states.TravelEnterBookingDetailsState

/**
 * UI State for the Flight Search screen.
 * Focuses only on capturing search criteria.
 */
data class FlightSearchState(
    val originQuery: String = "",
    val destinationQuery: String = "",
    val originSuggestions: List<Airport> = emptyList(),
    val destinationSuggestions: List<Airport> = emptyList(),
    val adults: Int = 1,
    val children: Int = 0,
    val infants: Int = 0,
    val bookingDetailsState: TravelEnterBookingDetailsState = TravelEnterBookingDetailsState(),
    val minSelectableDate: Long = System.currentTimeMillis(),
    val errorMessage: Int? = null
)
