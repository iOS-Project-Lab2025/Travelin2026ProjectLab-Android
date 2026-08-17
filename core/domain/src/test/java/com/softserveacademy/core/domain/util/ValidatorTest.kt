package com.softserveacademy.core.domain.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class ValidatorTest {

    private fun getBirthDate(age: Int, monthOffset: Int = 0, dayOffset: Int = 0): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -age)
        calendar.add(Calendar.MONTH, monthOffset)
        calendar.add(Calendar.DAY_OF_MONTH, dayOffset)
        return calendar.timeInMillis
    }

    @Test
    fun `isAtLeast18 returns true for age 18`() {
        assertTrue(Validator.isAtLeast18(getBirthDate(18)))
    }

    @Test
    fun `isAtLeast18 returns true for age 25`() {
        assertTrue(Validator.isAtLeast18(getBirthDate(25)))
    }

    @Test
    fun `isAtLeast18 returns false for age 17`() {
        assertFalse(Validator.isAtLeast18(getBirthDate(17)))
    }

    @Test
    fun `isAtLeast18 returns false for age 18 minus one day`() {
        // Birth date is tomorrow 18 years ago
        assertFalse(Validator.isAtLeast18(getBirthDate(18, 0, 1)))
    }

    @Test
    fun `isAtLeast18 returns true for age 18 plus one day`() {
        // Birth date is yesterday 18 years ago
        assertTrue(Validator.isAtLeast18(getBirthDate(18, 0, -1)))
    }

    @Test
    fun `isValidEmail returns true for valid email`() {
        assertTrue(Validator.isValidEmail("test@example.com"))
        assertTrue(Validator.isValidEmail("user.name+tag@domain.co.uk"))
    }

    @Test
    fun `isValidEmail returns false for invalid email`() {
        assertFalse(Validator.isValidEmail("invalid-email"))
        assertFalse(Validator.isValidEmail("test@"))
        assertFalse(Validator.isValidEmail("@domain.com"))
        assertFalse(Validator.isValidEmail("test@domain"))
    }
}
