package com.softserveacademy.feature.booking.common.domain.usecase

import javax.inject.Inject

/**
 * Use case to validate the contact information for a booking.
 */
class ValidateContactInfoUseCase @Inject constructor() {

    /**
     * Validates the first name.
     *
     * @param firstName The first name to validate.
     * @return [ValidationResult] representing the result of the validation.
     */
    fun validateFirstName(firstName: String): ValidationResult {
        if (firstName.isBlank()) {
            return ValidationResult.Invalid("First name is required")
        }
        return ValidationResult.Success
    }

    /**
     * Validates the last name.
     *
     * @param lastName The last name to validate.
     * @return [ValidationResult] representing the result of the validation.
     */
    fun validateLastName(lastName: String): ValidationResult {
        if (lastName.isBlank()) {
            return ValidationResult.Invalid("Last name is required")
        }
        return ValidationResult.Success
    }

    /**
     * Validates the email address.
     *
     * @param email The email address to validate.
     * @return [ValidationResult] representing the result of the validation.
     */
    fun validateEmail(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult.Invalid("Email is required")
        }
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        if (!emailRegex.matches(email)) {
            return ValidationResult.Invalid("Invalid email format")
        }
        return ValidationResult.Success
    }

    /**
     * Validates the phone number.
     *
     * @param phoneNumber The phone number to validate.
     * @return [ValidationResult] representing the result of the validation.
     */
    fun validatePhoneNumber(phoneNumber: String): ValidationResult {
        if (phoneNumber.isBlank()) {
            return ValidationResult.Invalid("Phone number is required")
        }
        return ValidationResult.Success
    }

    sealed interface ValidationResult {
        data object Success : ValidationResult
        data class Invalid(val errorMessage: String) : ValidationResult
    }
}
