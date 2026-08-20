package com.softserveacademy.feature.booking.flight.presentation.states

import com.softserveacademy.feature.booking.flight.domain.model.FlightBookingDraft

/**
 * UI State for the Final Booking Confirmation.
 */
data class FlightBookingConfirmState(
    val isLoading: Boolean = true,
    val draft: FlightBookingDraft? = null,
    val totalPrice: Double = 0.0,
    val currency: String = "USD",
    val showPaymentSimulationSheet: Boolean = false,
    val isPaymentSuccessful: Boolean = false,
    val paymentSimulationError: String? = null,
    val clientSecret: String? = null,
    val isPaymentSheetLoading: Boolean = false,
    val error: String? = null
)