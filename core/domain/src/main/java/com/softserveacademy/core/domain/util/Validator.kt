package com.softserveacademy.core.domain.util

import java.util.Calendar

/**
 * Utility class for domain-level validations.
 */
object Validator {

    /**
     * Checks if a birth date corresponds to an age of at least 18 years.
     *
     * @param birthDate The birth date in milliseconds.
     * @return True if the person is 18 or older, false otherwise.
     */
    fun isAtLeast18(birthDate: Long): Boolean {
        val today = Calendar.getInstance()
        val birthCalendar = Calendar.getInstance().apply { timeInMillis = birthDate }
        
        var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
        
        // Month is 0-indexed in Calendar
        if (today.get(Calendar.MONTH) < birthCalendar.get(Calendar.MONTH) || 
            (today.get(Calendar.MONTH) == birthCalendar.get(Calendar.MONTH) && 
             today.get(Calendar.DAY_OF_MONTH) < birthCalendar.get(Calendar.DAY_OF_MONTH))) {
            age--
        }
        
        return age >= 18
    }

    /**
     * Validates an email address format.
     */
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        return emailRegex.matches(email)
    }
}
