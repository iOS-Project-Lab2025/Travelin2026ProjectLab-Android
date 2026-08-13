package com.softserveacademy.feature.booking.flight.domain.usecase

import javax.inject.Inject

/**
 * Encapsulates the business rules for validating a flight search request.
 *
 * Rules:
 * 1. Airport codes must follow IATA standard (3 uppercase letters).
 * 2. Origin and Destination cannot be the same within a segment.
 * 3. Dates cannot be in the past.
 * 4. Each segment must occur on or after the previous segment's date.
 * 5. Round trip must have an end date.
 */
class ValidateFlightSearchUseCase @Inject constructor() {
    private val iataRegex = Regex("^[A-Z]{3}$")

    /**
     * Performs a comprehensive validation of all segments.
     *
     * @param segments The list of flight legs to validate.
     * @param isRoundTrip Whether the search mode requires a return date.
     * @param endDate The selected return date, if applicable.
     * @return [FlightValidationResult] containing detailed errors per segment.
     */
    fun validate(
        segments: List<com.softserveacademy.core.domain.model.FlightSegment>,
        isRoundTrip: Boolean,
        endDate: Long?
    ): FlightValidationResult {
        val segmentErrors = mutableMapOf<Int, SegmentError>()
        var globalDateError: FlightError? = null

        segments.forEachIndexed { index, segment ->
            var dateSeqErr: FlightError? = null

            // Rule 4: Sequence validation
            if (index > 0) {
                val previousDate = segments[index - 1].dateMillis
                if (previousDate != null && segment.dateMillis != null && segment.dateMillis!! < previousDate) {
                    dateSeqErr = FlightError.INVALID_DATE_SEQUENCE
                }
            }

            // Rule 1: IATA format
            val originErr = when {
                segment.origin.isBlank() || !iataRegex.matches(segment.origin.trim().uppercase()) -> FlightError.INVALID_ORIGIN
                else -> null
            }

            // Rule 2: Origin != Destination
            val destErr = when {
                segment.destination.isBlank() || !iataRegex.matches(segment.destination.trim().uppercase()) -> FlightError.INVALID_DESTINATION
                segment.origin == segment.destination && segment.origin.isNotEmpty() -> FlightError.SAME_LOCATION
                else -> null
            }

            // Rule 3: No past dates
            val dateErr = when {
                segment.dateMillis == null || segment.dateMillis!! < (System.currentTimeMillis() - 86400000) -> FlightError.INVALID_DATE
                else -> null
            }

            val finalDateErr = dateErr ?: dateSeqErr

            if (originErr != null || destErr != null || finalDateErr != null) {
                segmentErrors[index] = SegmentError(originErr, destErr, finalDateErr)
            }
        }

        // Rule 5: Missing return date
        if (isRoundTrip && endDate == null) globalDateError = FlightError.MISSING_RETURN_DATE

        return FlightValidationResult(
            segmentErrors = segmentErrors,
            globalDateError = globalDateError,
            isValid = segmentErrors.isEmpty() && globalDateError == null
        )
    }

    /** Specific error types for flight business rules. */
    enum class FlightError { INVALID_ORIGIN, INVALID_DESTINATION, SAME_LOCATION, INVALID_DATE, MISSING_RETURN_DATE, INVALID_DATE_SEQUENCE }

    /** Error breakdown for a single flight leg. */
    data class SegmentError(val originError: FlightError? = null, val destinationError: FlightError? = null, val dateError: FlightError? = null)

    /** Final result of the validation process. */
    data class FlightValidationResult(val segmentErrors: Map<Int, SegmentError> = emptyMap(), val globalDateError: FlightError? = null, val isValid: Boolean)
}