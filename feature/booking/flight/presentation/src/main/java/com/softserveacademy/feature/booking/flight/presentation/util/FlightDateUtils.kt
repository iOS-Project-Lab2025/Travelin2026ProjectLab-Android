package com.softserveacademy.feature.booking.flight.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Utility to provide a consistent date format for the flight module.
 * Example: Aug 12, 2026
 */
@Composable
fun rememberFlightDateFormatter(): SimpleDateFormat {
    return remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
}

/**
 * Utility for short time format in result cards.
 * Example: 14:30
 */
@Composable
fun rememberFlightTimeFormatter(): SimpleDateFormat {
    return remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
}