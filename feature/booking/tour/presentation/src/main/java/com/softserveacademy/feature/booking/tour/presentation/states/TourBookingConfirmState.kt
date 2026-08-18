package com.softserveacademy.feature.booking.tour.presentation.states

import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.feature.booking.tour.domain.model.TourBookingDraft

data class TourBookingConfirmState(
    val tour: Tour? = null,
    val bookingDraft: TourBookingDraft? = null,
    val totalPrice: Double = 0.0,
    val isLoading: Boolean = false,
    val isPaymentSheetLoading: Boolean = false,
    val isPaymentSuccessful: Boolean = false,
    val error: String? = null,
    val clientSecret: String? = null,
    val showPaymentSimulationSheet: Boolean = false,
    val paymentSimulationError: String? = null
)
