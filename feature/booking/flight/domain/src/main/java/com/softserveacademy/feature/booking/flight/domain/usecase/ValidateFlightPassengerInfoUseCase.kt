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
    private val alphanumericRegex = Regex("^[a-zA-Z0-9]*$")

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
                firstNameError = when {
                    passenger.firstName.isBlank() -> PassengerFieldError.EMPTY
                    passenger.firstName.trim().length < 3 -> PassengerFieldError.TOO_SHORT
                    else -> null
                },
                lastNameError = when {
                    passenger.lastName.isBlank() -> PassengerFieldError.EMPTY
                    passenger.lastName.trim().length < 3 -> PassengerFieldError.TOO_SHORT
                    else -> null
                },
                documentError = when {
                    passenger.documentNumber.isBlank() -> PassengerFieldError.EMPTY
                    !alphanumericRegex.matches(passenger.documentNumber) -> PassengerFieldError.INVALID_FORMAT // Blocks '$', '#' etc.
                    else -> null
                },
                birthDateError = validateAge(passenger.birthDateMillis, passenger.passengerType)

            )
        }.filter { it.second.hasError() }.toMap()

        // Validate Contact Info
        val contactError = ContactError(
            emailError = when {
                contactInfo?.email?.isBlank() == true -> PassengerFieldError.EMPTY
                contactInfo?.email?.let { emailRegex.matches(it) } == false -> PassengerFieldError.INVALID_FORMAT
                else -> null
            },
            phoneError = when {
                contactInfo?.phone?.isBlank() == true -> PassengerFieldError.EMPTY
                (contactInfo?.phone?.length ?: 0) < 6 -> PassengerFieldError.TOO_SHORT
                else -> null
            }
        )

        return PassengerValidationResult(
            passengerErrors = passengerErrors,
            contactError = if (contactError.hasError()) contactError else null,
            isValid = passengerErrors.isEmpty() && !contactError.hasError()
        )
    }

    private fun validateAge(millis: Long?, type: com.softserveacademy.core.domain.model.PassengerType): PassengerFieldError? {
        if (millis == null) return PassengerFieldError.EMPTY

        val age = calculateAge(millis)
        return when (type) {
            com.softserveacademy.core.domain.model.PassengerType.ADU -> if (age < 12) PassengerFieldError.INVALID_AGE else null
            com.softserveacademy.core.domain.model.PassengerType.CHD -> if (age !in 2..11) PassengerFieldError.INVALID_AGE else null
            com.softserveacademy.core.domain.model.PassengerType.INF -> if (age >= 2) PassengerFieldError.INVALID_AGE else null
        }
    }

    private fun calculateAge(birthMillis: Long): Int {
        val birth = java.util.Calendar.getInstance().apply { timeInMillis = birthMillis }
        val today = java.util.Calendar.getInstance()
        var age = today.get(java.util.Calendar.YEAR) - birth.get(java.util.Calendar.YEAR)
        if (today.get(java.util.Calendar.DAY_OF_YEAR) < birth.get(java.util.Calendar.DAY_OF_YEAR)) age--
        return age
    }

}


