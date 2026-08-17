package com.softserveacademy.feature.booking.flight.domain.usecase

import com.softserveacademy.core.domain.model.FlightContactInfo
import com.softserveacademy.core.domain.model.FlightPassenger
import com.softserveacademy.core.domain.model.PassengerType
import com.softserveacademy.feature.booking.flight.domain.model.PassengerFieldError
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.Calendar

/**
 * Business Logic Tests for Passenger and Contact validation (US3).
 *
 * Verifies:
 * 1. Strict age requirements for Adults, Children, and Infants.
 * 2. Document format integrity (Alphanumeric with hyphens).
 * 3. Name length constraints.
 * 4. Contact information formatting (Email and Phone).
 */
class ValidateFlightPassengerInfoUseCaseTest {

    private lateinit var useCase: ValidateFlightPassengerInfoUseCase

    @Before
    fun setup() {
        useCase = ValidateFlightPassengerInfoUseCase()
    }

    /**
     * Test Case: Verifies that names shorter than 3 characters are rejected.
     */
    @Test
    fun `when names are too short, should return TOO_SHORT error`() {
        val passenger = FlightPassenger(firstName = "Jo", lastName = "Do")
        val result = useCase.validate(listOf(passenger), null)

        val paxError = result.passengerErrors[0]
        assertEquals(PassengerFieldError.TOO_SHORT, paxError?.firstNameError)
        assertEquals(PassengerFieldError.TOO_SHORT, paxError?.lastNameError)
    }

    /**
     * Test Case: Ensures document numbers only allow letters, numbers and hyphens.
     */
    @Test
    fun `when document has special characters, should return INVALID_FORMAT`() {
        val passenger = FlightPassenger(
            firstName = "John",
            lastName = "Doe",
            documentNumber = "12345$#" // Invalid characters
        )
        val result = useCase.validate(listOf(passenger), null)

        assertEquals(PassengerFieldError.INVALID_FORMAT, result.passengerErrors[0]?.documentError)
    }

    /**
     * Test Case: Validates the core Age Rule: ADULT must be 12 years or older.
     */
    @Test
    fun `when passenger is ADU but age is less than 12, should return INVALID_AGE`() {
        // Mock birth date to 5 years ago
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -5)

        val passenger = FlightPassenger(
            firstName = "John", lastName = "Doe",
            passengerType = PassengerType.ADU, // Adult
            birthDateMillis = calendar.timeInMillis
        )

        val result = useCase.validate(listOf(passenger), null)
        assertEquals(PassengerFieldError.INVALID_AGE, result.passengerErrors[0]?.birthDateError)
    }

    /**
     * Test Case: Validates the core Age Rule: INFANT must be younger than 2 years.
     */
    @Test
    fun `when passenger is INF but age is 3 years, should return INVALID_AGE`() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -3)

        val passenger = FlightPassenger(
            firstName = "Baby", lastName = "Doe",
            passengerType = PassengerType.INF, // Infant
            birthDateMillis = calendar.timeInMillis
        )

        val result = useCase.validate(listOf(passenger), null)
        assertEquals(PassengerFieldError.INVALID_AGE, result.passengerErrors[0]?.birthDateError)
    }

    /**
     * Test Case: Ensures email validation follows the correct format.
     */
    @Test
    fun `when email format is invalid, should return INVALID_FORMAT in contact error`() {
        val contact = FlightContactInfo(email = "invalid-email-at-domain.com")
        val result = useCase.validate(emptyList(), contact)

        assertEquals(PassengerFieldError.INVALID_FORMAT, result.contactError?.emailError)
    }

    /**
     * Test Case: Verifies that a perfectly filled form returns isValid = true.
     */
    @Test
    fun `when all data is valid and age matches type, should return success`() {
        val adultCalendar = Calendar.getInstance().apply { add(Calendar.YEAR, -25) }
        val passenger = FlightPassenger(
            firstName = "John", lastName = "Doe",
            documentNumber = "123456789",
            passengerType = PassengerType.ADU,
            birthDateMillis = adultCalendar.timeInMillis
        )
        val contact = FlightContactInfo(email = "john@doe.com", phone = "12345678")

        val result = useCase.validate(listOf(passenger), contact)
        assertTrue("Form should be valid", result.isValid)
        assertTrue("No passenger errors expected", result.passengerErrors.isEmpty())
        assertNull("No contact error expected", result.contactError)
    }
}