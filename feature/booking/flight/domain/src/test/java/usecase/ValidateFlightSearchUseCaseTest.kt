package com.softserveacademy.feature.booking.flight.domain.usecase

import com.softserveacademy.core.domain.model.FlightSegment
import org.junit.Assert.*
import org.junit.Test

class ValidateFlightSearchUseCaseTest {

    private val useCase = ValidateFlightSearchUseCase()

    @Test
    fun `when segments are valid, return success`() {
        val segments = listOf(
            FlightSegment("SCL", "LIM", System.currentTimeMillis() + 86400000)
        )
        val result = useCase.validate(segments, isRoundTrip = false, endDate = null)
        assertTrue(result.isValid)
    }

    @Test
    fun `when origin is not 3 letters IATA, return INVALID_ORIGIN error`() {
        val segments = listOf(
            FlightSegment("SANTIAGO", "LIM", System.currentTimeMillis() + 86400000)
        )
        val result = useCase.validate(segments, isRoundTrip = false, endDate = null)
        assertFalse(result.isValid)
        assertEquals(ValidateFlightSearchUseCase.FlightError.INVALID_ORIGIN, result.segmentErrors[0]?.originError)
    }

    @Test
    fun `when origin and destination are same, return SAME_LOCATION error`() {
        val segments = listOf(
            FlightSegment("SCL", "SCL", System.currentTimeMillis() + 86400000)
        )
        val result = useCase.validate(segments, isRoundTrip = false, endDate = null)
        assertEquals(ValidateFlightSearchUseCase.FlightError.SAME_LOCATION, result.segmentErrors[0]?.destinationError)
    }

    @Test
    fun `when second flight is before first flight, return INVALID_DATE_SEQUENCE error`() {
        val now = System.currentTimeMillis()
        val segments = listOf(
            FlightSegment("SCL", "LIM", now + 200000),
            FlightSegment("LIM", "GRU", now + 100000) // Antes que el anterior
        )
        val result = useCase.validate(segments, isRoundTrip = false, endDate = null)
        assertEquals(ValidateFlightSearchUseCase.FlightError.INVALID_DATE_SEQUENCE, result.segmentErrors[1]?.dateError)
    }

    @Test
    fun `when round trip missing return date, return MISSING_RETURN_DATE error`() {
        val segments = listOf(FlightSegment("SCL", "LIM", System.currentTimeMillis() + 86400000))
        val result = useCase.validate(segments, isRoundTrip = true, endDate = null)
        assertEquals(ValidateFlightSearchUseCase.FlightError.MISSING_RETURN_DATE, result.globalDateError)
    }

    @Test
    fun `when origin contains numbers, return INVALID_ORIGIN error`() {
        val segments = listOf(FlightSegment("S1L", "LIM", System.currentTimeMillis() + 86400000))
        val result = useCase.validate(segments, false, null)
        assertEquals(ValidateFlightSearchUseCase.FlightError.INVALID_ORIGIN, result.segmentErrors[0]?.originError)
    }

    @Test
    fun `when destination contains SQL injection characters, return INVALID_DESTINATION error`() {
        val segments = listOf(FlightSegment("SCL", "LI'", System.currentTimeMillis() + 86400000))
        val result = useCase.validate(segments, false, null)
        assertEquals(ValidateFlightSearchUseCase.FlightError.INVALID_DESTINATION, result.segmentErrors[0]?.destinationError)
    }

    @Test
    fun `when origin is empty string, return INVALID_ORIGIN error`() {
        val segments = listOf(FlightSegment("", "LIM", System.currentTimeMillis() + 86400000))
        val result = useCase.validate(segments, false, null)
        assertEquals(ValidateFlightSearchUseCase.FlightError.INVALID_ORIGIN, result.segmentErrors[0]?.originError)
    }

    @Test
    fun `when origin has lowercase letters, return success because of sanitization`() {

        val segments = listOf(FlightSegment("scl", "LIM", System.currentTimeMillis() + 86400000))
        val result = useCase.validate(segments, false, null)
        assertTrue(result.isValid)
    }

    @Test
    fun `when origin is too long, return INVALID_ORIGIN error`() {
        val segments = listOf(FlightSegment("SCL-AIRPORT", "LIM", System.currentTimeMillis() + 86400000))
        val result = useCase.validate(segments, false, null)
        assertEquals(ValidateFlightSearchUseCase.FlightError.INVALID_ORIGIN, result.segmentErrors[0]?.originError)
    }
}
