package com.softserveacademy.feature.booking.presentation.components.util

/**
 * Sealed interface representing items in the booking count bottom sheet.
 * Can be either a counter or a switch.
 */
sealed interface TravelBookingCountItem {
    val label: String
    val subtitle: String?

    data class Counter(
        override val label: String,
        override val subtitle: String? = null,
        val count: Int,
        val onCountChange: (Int) -> Unit,
        val minCount: Int = 0,
        val maxCount: Int = 50
    ) : TravelBookingCountItem

    data class Switch(
        override val label: String,
        override val subtitle: String? = null,
        val checked: Boolean,
        val onCheckedChange: (Boolean) -> Unit
    ) : TravelBookingCountItem
}