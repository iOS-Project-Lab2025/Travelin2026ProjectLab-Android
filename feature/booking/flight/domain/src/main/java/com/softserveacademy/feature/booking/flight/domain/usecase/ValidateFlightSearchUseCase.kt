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
            var dateSeqErr: FlightError? = null
            if (index > 0) {
                val previousDate = segments[index - 1].dateMillis
                if (previousDate != null && segment.dateMillis != null && segment.dateMillis!! < previousDate) {
                    dateSeqErr = FlightError.INVALID_DATE_SEQUENCE
                }
            }

            val originErr = when {
                segment.origin.isBlank() || !iataRegex.matches(segment.origin.trim().uppercase()) -> FlightError.INVALID_ORIGIN
                else -> null
            }

            val destErr = when {
                segment.destination.isBlank() || !iataRegex.matches(segment.destination.trim().uppercase()) -> FlightError.INVALID_DESTINATION
                segment.origin == segment.destination && segment.origin.isNotEmpty() -> FlightError.SAME_LOCATION
                else -> null
            }

            val dateErr = when {
                segment.dateMillis == null || segment.dateMillis!! < (System.currentTimeMillis() - 86400000) -> FlightError.INVALID_DATE
                else -> null
            }

            val finalDateErr = dateErr ?: dateSeqErr

            if (originErr != null || destErr != null || finalDateErr != null) {
                segmentErrors[index] = SegmentError(originErr, destErr, finalDateErr)
            }
        }

        if (isRoundTrip && endDate == null) globalDateError = FlightError.MISSING_RETURN_DATE

        return FlightValidationResult(
            segmentErrors = segmentErrors,
            globalDateError = globalDateError,
            isValid = segmentErrors.isEmpty() && globalDateError == null
        )
    }

    enum class FlightError { INVALID_ORIGIN, INVALID_DESTINATION, SAME_LOCATION, INVALID_DATE, MISSING_RETURN_DATE, INVALID_DATE_SEQUENCE }
    data class SegmentError(val originError: FlightError? = null, val destinationError: FlightError? = null, val dateError: FlightError? = null)
    data class FlightValidationResult(val segmentErrors: Map<Int, SegmentError> = emptyMap(), val globalDateError: FlightError? = null, val isValid: Boolean)
}