package com.softserveacademy.feature.booking.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateBookingSearchUseCaseTest {

    private val useCase = ValidateBookingSearchUseCase()

    @Test
    fun `validateDates returns Success when dates are not null`() {
        val result = useCase.validateDates(100L, 200L)
        assertTrue(result is ValidateBookingSearchUseCase.ValidationResult.Success)
    }

    @Test
    fun `validateDates returns Invalid with EMPTY_DATES when checkInDate is null`() {
        val result = useCase.validateDates(null, 200L)
        assertTrue(result is ValidateBookingSearchUseCase.ValidationResult.Invalid)
        assertEquals(
            ValidateBookingSearchUseCase.ValidationError.EMPTY_DATES,
            (result as ValidateBookingSearchUseCase.ValidationResult.Invalid).error
        )
    }

    @Test
    fun `validateDates returns Invalid with EMPTY_DATES when checkOutDate is null`() {
        val result = useCase.validateDates(100L, null)
        assertTrue(result is ValidateBookingSearchUseCase.ValidationResult.Invalid)
        assertEquals(
            ValidateBookingSearchUseCase.ValidationError.EMPTY_DATES,
            (result as ValidateBookingSearchUseCase.ValidationResult.Invalid).error
        )
    }

    @Test
    fun `validateGuests returns Success when adults count is at least 1`() {
        val result = useCase.validateGuests(1)
        assertTrue(result is ValidateBookingSearchUseCase.ValidationResult.Success)
    }

    @Test
    fun `validateGuests returns Invalid with AT_LEAST_ONE_ADULT when adults count is less than 1`() {
        val result = useCase.validateGuests(0)
        assertTrue(result is ValidateBookingSearchUseCase.ValidationResult.Invalid)
        assertEquals(
            ValidateBookingSearchUseCase.ValidationError.AT_LEAST_ONE_ADULT,
            (result as ValidateBookingSearchUseCase.ValidationResult.Invalid).error
        )
    }
}
