package com.softserveacademy.feature.booking.flight.presentation.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Utility to format currency values consistently across the flight module.
 * Separated from dates and mappers for better maintainability.
 */
fun formatCurrency(price: Double, rate: Double): String {
    val converted = price * rate
    val formatter = NumberFormat.getNumberInstance(Locale.US).apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 0
    }
    return formatter.format(converted)
}