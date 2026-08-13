package com.softserveacademy.feature.booking.flight.presentation.util

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for currency formatting logic.
 * Ensures that price conversion and international standard formatting (Locale.US)
 * work correctly for different rates and amounts.
 */
class FlightCurrencyUtilsTest {

    @Test
    fun `formatCurrency should apply exchange rate and use US standard formatting`() {
        val basePrice = 1000.0
        val rate = 1.25 // Example: USD to another currency

        val result = formatCurrency(basePrice, rate)

        // 1000 * 1.25 = 1,250.00
        assertEquals("1,250", result)
    }

    @Test
    fun `formatCurrency should handle large numbers with commas`() {
        val largePrice = 1000000.0
        val result = formatCurrency(largePrice, 1.0)

        assertEquals("1,000,000", result)
    }

    @Test
    fun `formatCurrency should handle decimal points correctly`() {
        val price = 1250.55
        val result = formatCurrency(price, 1.0)

        assertEquals("1,250.55", result)
    }
}