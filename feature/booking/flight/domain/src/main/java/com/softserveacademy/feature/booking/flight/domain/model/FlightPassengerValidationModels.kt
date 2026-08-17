package com.softserveacademy.feature.booking.flight.domain.model

/**
 * Possible field-level validation errors for travelers.
 */
enum class PassengerFieldError { EMPTY, INVALID_FORMAT, TOO_SHORT, INVALID_AGE }

/**
 * Breakdown of validation errors for a single passenger.
 */
data class PassengerError(
    val firstNameError: PassengerFieldError? = null,
    val lastNameError: PassengerFieldError? = null,
    val documentError: PassengerFieldError? = null,
    val birthDateError: PassengerFieldError? = null
) {
    fun hasError() = firstNameError != null || lastNameError != null || documentError != null || birthDateError != null
}

/**
 * Breakdown of validation errors for primary contact information.
 */
data class ContactError(
    val emailError: PassengerFieldError? = null,
    val phoneError: PassengerFieldError? = null
) {
    fun hasError() = emailError != null || phoneError != null
}

/**
 * Final output of the passenger info validation process.
 */
data class PassengerValidationResult(
    val passengerErrors: Map<Int, PassengerError> = emptyMap(),
    val contactError: ContactError? = null,
    val isValid: Boolean
)