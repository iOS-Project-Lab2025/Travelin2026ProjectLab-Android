package com.softserveacademy.feature.booking.common.presentation.states

import java.io.Serializable

/**
 * Data class representing the state of an enter booking details screen.
 * Decoupled from specific booking types.
 *
 * @property startDateMillis The selected start date in milliseconds.
 * @property endDateMillis The selected end date in milliseconds.
 * @property adultsCount The number of adults for the booking.
 * @property childrenCount The number of children for the booking.
 * @property hasPets Whether the booking includes pets.
 * @property isDateErrorVisible Whether the date selection error is visible.
 * @property dateErrorMessage The error message for date selection.
 * @property isGuestErrorVisible Whether the guest selection error is visible.
 * @property guestErrorMessage The error message for guest selection.
 * @property showGuestBottomSheet Whether the guest selection bottom sheet is visible.
 */
data class TravelEnterBookingDetailsState(
    val startDateMillis: Long? = null,
    val endDateMillis: Long? = null,
    val adultsCount: Int = 1,
    val childrenCount: Int = 0,
    val hasPets: Boolean = false,
    val isDateErrorVisible: Boolean = false,
    val dateErrorMessage: Int? = null,
    val isGuestErrorVisible: Boolean = false,
    val guestErrorMessage: Int? = null,
    val showGuestBottomSheet: Boolean = false,
    val isLoading: Boolean = false,
) : Serializable
