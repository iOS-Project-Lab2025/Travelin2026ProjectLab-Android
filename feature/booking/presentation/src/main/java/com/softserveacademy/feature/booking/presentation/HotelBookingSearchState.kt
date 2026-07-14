package com.softserveacademy.feature.booking.presentation

import com.softserveacademy.feature.booking.domain.HotelBookingDraft

/**
 * Data class representing the state of the hotel booking search screen.
 *
 * @property draft The draft of the hotel booking.
 * @property isDateErrorVisible Whether the date selection error is visible.
 * @property dateErrorMessage The error message for date selection.
 * @property isGuestErrorVisible Whether the guest selection error is visible.
 * @property guestErrorMessage The error message for guest selection.
 * @property showGuestBottomSheet Whether the guest selection bottom sheet is visible.
 */
data class HotelBookingSearchState(
    val draft: HotelBookingDraft = HotelBookingDraft(),
    val isDateErrorVisible: Boolean = false,
    val dateErrorMessage: Int? = null,
    val isGuestErrorVisible: Boolean = false,
    val guestErrorMessage: Int? = null,
    val showGuestBottomSheet: Boolean = false
)
