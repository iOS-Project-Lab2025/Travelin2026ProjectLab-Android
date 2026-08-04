package com.softserveacademy.feature.booking.hotel.presentation.states

import com.softserveacademy.core.domain.model.HotelDetails
import com.softserveacademy.core.domain.model.HotelRoom
import com.softserveacademy.feature.booking.hotel.domain.model.HotelBookingDraft
import java.io.Serializable

/**
 * State class for the Hotel Booking Confirmation screen.
 */
data class HotelBookingConfirmState(
    val isLoading: Boolean = false,
    val hotelDetails: HotelDetails? = null,
    val selectedRoom: HotelRoom? = null,
    val bookingDraft: HotelBookingDraft? = null,
    val clientSecret: String? = null,
    val totalPrice: Int = 0,
    val isPaymentSuccessful: Boolean = false,
    val isPaymentSheetLoading: Boolean = false,
    val error: String? = null,
    val showPaymentSimulationSheet: Boolean = false,
    val paymentSimulationError: String? = null
) : Serializable
