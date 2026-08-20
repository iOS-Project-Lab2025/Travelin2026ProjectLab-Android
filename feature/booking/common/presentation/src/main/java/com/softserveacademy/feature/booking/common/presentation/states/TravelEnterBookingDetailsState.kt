package com.softserveacademy.feature.booking.common.presentation.states

import java.io.Serializable

/**
 * Data class representing the common state of an enter booking details screen.
 * Holds shared properties like date range and UI error states.
 *
 * @property startDateMillis The selected start date in milliseconds.
 * @property endDateMillis The selected end date in milliseconds.
 * @property singleDatePicker Whether to show a single date picker or a date range picker.
 * @property isDateErrorVisible Whether the date selection error is visible.
 * @property dateErrorMessage The error message for date selection.
 * @property isGuestErrorVisible Whether the guest selection error is visible.
 * @property guestErrorMessage The error message for guest selection.
 * @property showGuestBottomSheet Whether the guest selection bottom sheet is visible.
 * @property isLoading Whether the data is being loaded.
 */
data class TravelEnterBookingDetailsState(
    val startDateMillis: Long? = null,
    val endDateMillis: Long? = null,
    val singleDatePicker: Boolean = false,
    val isDateErrorVisible: Boolean = false,
    val dateErrorMessage: Int? = null,
    val isGuestErrorVisible: Boolean = false,
    val guestErrorMessage: Int? = null,
    val showGuestBottomSheet: Boolean = false,
    val isLoading: Boolean = false,
) : Serializable
