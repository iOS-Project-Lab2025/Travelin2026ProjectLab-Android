package com.softserveacademy.feature.booking.common.domain

import com.softserveacademy.feature.booking.common.domain.usecase.ValidateEnterBookingDetailsUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateEnterBookingDetailsUseCaseTest {

    private val useCase = ValidateEnterBookingDetailsUseCase()

    @Test
    fun `validateDates returns Success when dates are not null`() {
        val result = useCase.validateDates(100L, 200L)
        assertTrue(result is ValidateEnterBookingDetailsUseCase.ValidationResult.Success)
    }

    @Test
    fun `validateDates returns Invalid with EMPTY_DATES when checkInDate is null`() {
        val result = useCase.validateDates(null, 200L)
        assertTrue(result is ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid)
        assertEquals(
            ValidateEnterBookingDetailsUseCase.ValidationError.EMPTY_DATES,
            (result as ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid).error
        )
    }

    @Test
    fun `validateDates returns Invalid with EMPTY_DATES when checkOutDate is null`() {
        val result = useCase.validateDates(100L, null)
        assertTrue(result is ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid)
        assertEquals(
            ValidateEnterBookingDetailsUseCase.ValidationError.EMPTY_DATES,
            (result as ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid).error
        )
    }

    @Test
    fun `validateGuests returns Success when adults count is at least 1`() {
        val result = useCase.validateGuests(1)
        assertTrue(result is ValidateEnterBookingDetailsUseCase.ValidationResult.Success)
    }

    @Test
    fun `validateGuests returns Invalid with AT_LEAST_ONE_ADULT when adults count is less than 1`() {
        val result = useCase.validateGuests(0)
        assertTrue(result is ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid)
        assertEquals(
            ValidateEnterBookingDetailsUseCase.ValidationError.AT_LEAST_ONE_ADULT,
            (result as ValidateEnterBookingDetailsUseCase.ValidationResult.Invalid).error
        )
    }
}
