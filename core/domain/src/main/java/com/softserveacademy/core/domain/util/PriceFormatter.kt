package com.softserveacademy.core.domain.util

import java.util.Locale

/**
 * Formats a price to a string.
 * If the price has no decimal part, it returns the integer part as a string.
 * Otherwise, it returns the price with up to two decimal places.
 *
 * @param price The price to format.
 * @return The formatted price string.
 */
fun formatPrice(price: Double): String {
    return if (price % 1.0 == 0.0) {
        price.toInt().toString()
    } else {
        // Use US locale to ensure dot as decimal separator
        // For USD, we show 2 decimal places if there is a decimal part
        String.format(Locale.US, "%.2f", price)
    }
}
