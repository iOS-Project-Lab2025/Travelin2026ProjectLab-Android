package com.softserveacademy.feature.booking.flight.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Provides a consistent date formatter for flight search criteria and summaries.
 * Uses a lifecycle-aware 'remember' to avoid recreating formatters during recomposition.
 *
 * Example output: "Aug 12, 2026"
 *
 * @return [SimpleDateFormat] configured for short month and year display.
 */
@Composable
fun rememberFlightDateFormatter(): SimpleDateFormat {
    return remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
}

/**
 * Provides a short time formatter for flight result cards.
 * Essential for displaying departure and arrival times in a compact format.
 *
 * Example output: "14:30"
 *
 * @return [SimpleDateFormat] configured for 24-hour clock display.
 */
@Composable
fun rememberFlightTimeFormatter(): SimpleDateFormat {
    return remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
}