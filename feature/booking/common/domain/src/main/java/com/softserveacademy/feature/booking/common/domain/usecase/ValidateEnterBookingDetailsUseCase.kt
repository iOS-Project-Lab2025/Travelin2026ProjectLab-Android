package com.softserveacademy.feature.booking.common.domain.usecase

import javax.inject.Inject

/**
 * Use case to validate the booking selected dates and guests.
 */
class ValidateEnterBookingDetailsUseCase @Inject constructor() {

    /**
     * Validates if the selected dates are valid.
     *
     * @param checkIn The check-in date in milliseconds.
     * @param checkOut The check-out date in milliseconds.
     * @return [ValidationResult] representing the result of the validation.
     */
    fun validateDates(checkIn: Long?, checkOut: Long?): ValidationResult {
        if (checkIn == null || checkOut == null) {
            return ValidationResult.Invalid(ValidationError.EMPTY_DATES)
        }
        return ValidationResult.Success
    }

    /**
     * Validates if the guest counts are valid.
     *
     * @param adults The number of adults.
     * @return [ValidationResult] representing the result of the validation.
     */
    fun validateGuests(adults: Int): ValidationResult {
        if (adults < 1) {
            return ValidationResult.Invalid(ValidationError.AT_LEAST_ONE_ADULT)
        }
        return ValidationResult.Success
    }

    sealed interface ValidationResult {
        data object Success : ValidationResult
        data class Invalid(val error: ValidationError) : ValidationResult
    }

    enum class ValidationError {
        EMPTY_DATES,
        AT_LEAST_ONE_ADULT
    }
}