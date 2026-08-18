package com.softserveacademy.feature.booking.common.domain.usecase

import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.DurationUnit

/**
 * Use case to validate the booking selected dates and guests.
 */
class ValidateEnterBookingDetailsUseCase @Inject constructor() {

    /**
     * Validates if the selected dates are valid.
     *
     * @param startDate The start date in milliseconds.
     * @param endDate The end date in milliseconds.
     * @return [ValidationResult] representing the result of the validation.
     */
    fun validateDates(startDate: Long?, endDate: Long?): ValidationResult {
        if (startDate == null || endDate == null) {
            return ValidationResult.Invalid(ValidationError.EMPTY_DATES)
        }
        return ValidationResult.Success
    }

    /**
     * Validates if the selected dates duration is valid.
     *
     * @param startDate The start date in milliseconds.
     * @param endDate The end date in milliseconds.
     * @param duration The expected duration.
     * @return [ValidationResult] representing the result of the validation.
     */
    fun validateDatesDuration(startDate: Long?, endDate: Long?, duration: Duration): ValidationResult {
        if (startDate == null || endDate == null) {
            return ValidationResult.Invalid(ValidationError.INVALID_DURATION)
        }
        val dateRangeDuration = endDate - startDate
        if (dateRangeDuration != duration.toLong(DurationUnit.MILLISECONDS)){
            if (duration > Duration.parse("PT24H")){
                return ValidationResult.Invalid(ValidationError.INVALID_DURATION)
            }
            if(duration < Duration.parse("PT24H") && dateRangeDuration > duration.toLong(DurationUnit.MILLISECONDS)){
                return ValidationResult.Invalid(ValidationError.INVALID_DURATION)
            }
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
        INVALID_DURATION,
        AT_LEAST_ONE_ADULT
    }
}