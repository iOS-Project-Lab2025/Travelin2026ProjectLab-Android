package com.softserveacademy.feature.booking.flight.domain.usecase

import com.softserveacademy.core.domain.model.FlightSegment
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for [ValidateFlightSearchUseCase].
 * Verifies all business rules for flight searching including IATA codes,
 * location logic, and date sequences.
 */
class ValidateFlightSearchUseCaseTest {

    private val useCase = ValidateFlightSearchUseCase()

    /**
     * Verifies that valid segments with correct dates and IATA codes return a success result.
     */
    @Test
    fun `when segments are valid, return success`() {
        val segments = listOf(
            FlightSegment("SCL", "LIM", System.currentTimeMillis() + 86400000)
        )
        val result = useCase.validate(segments, isRoundTrip = false, endDate = null)
        assertTrue("Result should be valid for correct input", result.isValid)
    }

    /**
     * Rules: IATA codes must be exactly 3 letters.
     */
    @Test
    fun `when origin is not 3 letters IATA, return INVALID_ORIGIN error`() {
        val segments = listOf(
            FlightSegment("SANTIAGO", "LIM", System.currentTimeMillis() + 86400000)
        )
        val result = useCase.validate(segments, isRoundTrip = false, endDate = null)
        assertFalse(result.isValid)
        assertEquals(ValidateFlightSearchUseCase.FlightError.INVALID_ORIGIN, result.segmentErrors[0]?.originError)
    }

    /**
     * Rules: Origin and Destination cannot be the same within a segment.
     */
    @Test
    fun `when origin and destination are same, return SAME_LOCATION error`() {
        val segments = listOf(
            FlightSegment("SCL", "SCL", System.currentTimeMillis() + 86400000)
        )
        val result = useCase.validate(segments, isRoundTrip = false, endDate = null)
        assertEquals(ValidateFlightSearchUseCase.FlightError.SAME_LOCATION, result.segmentErrors[0]?.destinationError)
    }

    /**
     * Rules: Flights in a sequence must occur after the previous flight.
     */
    @Test
    fun `when second flight is before first flight, return INVALID_DATE_SEQUENCE error`() {
        val now = System.currentTimeMillis()
        val segments = listOf(
            FlightSegment("SCL", "LIM", now + 200000),
            FlightSegment("LIM", "GRU", now + 100000)
        )
        val result = useCase.validate(segments, isRoundTrip = false, endDate = null)
        assertEquals(ValidateFlightSearchUseCase.FlightError.INVALID_DATE_SEQUENCE, result.segmentErrors[1]?.dateError)
    }

    /**
     * Rules: Round Trip selection requires a return date.
     */
    @Test
    fun `when round trip missing return date, return MISSING_RETURN_DATE error`() {
        val segments = listOf(FlightSegment("SCL", "LIM", System.currentTimeMillis() + 86400000))
        val result = useCase.validate(segments, isRoundTrip = true, endDate = null)
        assertEquals(ValidateFlightSearchUseCase.FlightError.MISSING_RETURN_DATE, result.globalDateError)
    }

    @Test
    fun `when origin is empty string, return INVALID_ORIGIN error`() {
        val segments = listOf(FlightSegment("", "LIM", System.currentTimeMillis() + 86400000))
        val result = useCase.validate(segments, false, null)
        assertEquals(ValidateFlightSearchUseCase.FlightError.INVALID_ORIGIN, result.segmentErrors[0]?.originError)
    }
}
