package com.softserveacademy.feature.booking.tour.presentation.states

import com.softserveacademy.feature.booking.common.presentation.states.TravelEnterBookingDetailsState
import java.io.Serializable

/**
 * Data class representing the state of an enter tour booking details screen.
 *
 * @property screenState The common screen state for booking details.
 * @property adultsCount The number of adults for the booking.
 * @property childrenCount The number of children for the booking.
 * @property infantsCount The number of infants for the booking.
 */
data class TourEnterBookingDetailsState(
    val screenState: TravelEnterBookingDetailsState = TravelEnterBookingDetailsState(),
    val adultsCount: Int = 1,
    val childrenCount: Int = 0,
    val infantsCount: Int = 0,
) : Serializable
