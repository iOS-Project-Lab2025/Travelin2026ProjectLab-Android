package com.softserveacademy.feature.booking.hotel.presentation.states

import com.softserveacademy.feature.booking.common.presentation.states.TravelEnterBookingDetailsState
import java.io.Serializable

/**
 * Data class representing the state of an enter hotel booking details screen.
 *
 * @property screenState The common screen state for booking details.
 * @property adultsCount The number of adults for the booking.
 * @property childrenCount The number of children for the booking.
 * @property hasPets Whether the booking includes pets.
 */
data class HotelEnterBookingDetailsState(
    val screenState: TravelEnterBookingDetailsState = TravelEnterBookingDetailsState(),
    val adultsCount: Int = 1,
    val childrenCount: Int = 0,
    val hasPets: Boolean = false,
) : Serializable
