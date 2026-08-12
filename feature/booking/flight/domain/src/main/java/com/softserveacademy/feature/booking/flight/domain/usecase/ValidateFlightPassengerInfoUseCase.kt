package com.softserveacademy.feature.booking.flight.domain.usecase

import com.softserveacademy.core.domain.model.FlightContactInfo
import com.softserveacademy.core.domain.model.FlightPassenger
import com.softserveacademy.feature.booking.flight.domain.model.ContactError
import com.softserveacademy.feature.booking.flight.domain.model.PassengerValidationResult
import com.softserveacademy.feature.booking.flight.domain.model.PassengerError
import com.softserveacademy.feature.booking.flight.domain.model.PassengerFieldError
import javax.inject.Inject

/**
 * Business logic to validate traveler details and contact information (US3).
 * Ensures all required fields are present and follow basic formatting rules.
 */
class ValidateFlightPassengerInfoUseCase @Inject constructor() {

    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")

    /**
     * Validates the complete passenger and contact state.
     *
     * @param passengers List of travelers to validate.
     * @param contactInfo Primary contact details.
     * @return [PassengerValidationResult] containing individual error mappings.
     */
    fun validate(
        passengers: List<FlightPassenger>,
        contactInfo: FlightContactInfo?
    ): PassengerValidationResult {
        // Validate Passengers
        val passengerErrors = passengers.mapIndexed { index, passenger ->
            index to PassengerError(
                firstNameError = if (passenger.firstName.isBlank()) PassengerFieldError.EMPTY else null,
                lastNameError = if (passenger.lastName.isBlank()) PassengerFieldError.EMPTY else null,
                documentError = if (passenger.documentNumber.isBlank()) PassengerFieldError.EMPTY else null,
                birthDateError = if (passenger.birthDateMillis == null) PassengerFieldError.EMPTY else null

            )
        }.filter { it.second.hasError() }.toMap()

        // Validate Contact Info
        val contactError = ContactError(
            emailError = when {
                contactInfo?.email?.isBlank() == true -> PassengerFieldError.EMPTY
                contactInfo?.email?.let { emailRegex.matches(it) } == false -> PassengerFieldError.INVALID_FORMAT
                else -> null
            },
            phoneError = if (contactInfo?.phone?.isBlank() == true) PassengerFieldError.EMPTY else null
        )

        return PassengerValidationResult(
            passengerErrors = passengerErrors,
            contactError = if (contactError.hasError()) contactError else null,
            isValid = passengerErrors.isEmpty() && !contactError.hasError()
        )
    }
}


