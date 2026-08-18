package com.softserveacademy.feature.booking.flight.presentation.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Utility to format currency values consistently across the flight module.
 * This is a pure function decoupled from Compose and Android resources,
 * ensuring high testability and separation of concerns.
 *
 * @param price The base price in the original currency (default is USD).
 * @param rate The exchange rate to apply (e.g., 1.0 for no conversion).
 * @return A string representation of the amount with thousands separators
 * and up to 2 decimal places (e.g., "1,250.50").
 */
fun formatCurrency(price: Double, rate: Double): String {
    // 1. Calculate the converted amount
    val converted = price * rate

    // 2. Apply international standard formatting (thousands with commas)
    val formatter = NumberFormat.getNumberInstance(Locale.US).apply {
        maximumFractionDigits = 2
        minimumFractionDigits = if (converted % 1.0 == 0.0) 0 else 2
    }

    return formatter.format(converted)
}