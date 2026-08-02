package com.softserveacademy.feature.booking.flight.domain.usecase

import javax.inject.Inject

class ValidateFlightSearchUseCase @Inject constructor() {
    private val iataRegex = Regex("^[A-Z]{3}$")

    fun validate(
        segments: List<com.softserveacademy.core.domain.model.FlightSegment>,
        isRoundTrip: Boolean,
        endDate: Long?
    ): FlightValidationResult {
        val segmentErrors = mutableMapOf<Int, SegmentError>()
        var globalDateError: FlightError? = null

        segments.forEachIndexed { index, segment ->
            val origin = segment.origin.trim().uppercase()
            val dest = segment.destination.trim().uppercase()

            val originErr = when {
                origin.isBlank() || !iataRegex.matches(origin) -> FlightError.INVALID_ORIGIN
                else -> null
            }

            val destErr = when {
                dest.isBlank() || !iataRegex.matches(dest) -> FlightError.INVALID_DESTINATION
                origin == dest && origin.isNotEmpty() -> FlightError.SAME_LOCATION
                else -> null
            }

            val dateErr = when {
                segment.dateMillis == null || segment.dateMillis!! < (System.currentTimeMillis() - 86400000) -> FlightError.INVALID_DATE
                else -> null
            }

            if (originErr != null || destErr != null || dateErr != null) {
                segmentErrors[index] = SegmentError(originErr, destErr, dateErr)
            }
        }

        if (isRoundTrip && endDate == null) globalDateError = FlightError.MISSING_RETURN_DATE

        return FlightValidationResult(
            segmentErrors = segmentErrors,
            globalDateError = globalDateError,
            isValid = segmentErrors.isEmpty() && globalDateError == null
        )
    }

    enum class FlightError { INVALID_ORIGIN, INVALID_DESTINATION, SAME_LOCATION, INVALID_DATE, MISSING_RETURN_DATE }
    data class SegmentError(val originError: FlightError? = null, val destinationError: FlightError? = null, val dateError: FlightError? = null)
    data class FlightValidationResult(val segmentErrors: Map<Int, SegmentError> = emptyMap(), val globalDateError: FlightError? = null, val isValid: Boolean)
}