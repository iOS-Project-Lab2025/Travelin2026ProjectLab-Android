package com.softserveacademy.feature.booking.flight.domain.usecase

import javax.inject.Inject

/**
 * Professional validator for Flight Search.
 * Acts as a security and business logic gatekeeper.
 */
class ValidateFlightSearchUseCase @Inject constructor() {

    // Regex: Only letters, exactly 3 characters (IATA Standard)
    private val iataRegex = Regex("^[A-Z]{3}$")

    /**
     * Comprehensive validation of the search criteria.
     */
    fun validate(
        origin: String,
        destination: String,
        startDate: Long?,
        adults: Int
    ): FlightValidationResult {

        // 1. Security & Format: Origin
        val cleanOrigin = origin.trim().uppercase()
        if (cleanOrigin.isEmpty()) return FlightValidationResult.InvalidOrigin("Origin is required")
        if (!iataRegex.matches(cleanOrigin)) return FlightValidationResult.InvalidOrigin("Use 3-letter IATA code (e.g. SCL)")

        // 2. Security & Format: Destination
        val cleanDest = destination.trim().uppercase()
        if (cleanDest.isEmpty()) return FlightValidationResult.InvalidDestination("Destination is required")
        if (!iataRegex.matches(cleanDest)) return FlightValidationResult.InvalidDestination("Use 3-letter IATA code (e.g. LIM)")

        // 3. Business Rule: Same Location
        if (cleanOrigin == cleanDest) return FlightValidationResult.SameLocation

        // 4. Business Rule: Dates
        if (startDate == null) return FlightValidationResult.InvalidDate("Please select a travel date")
        if (startDate < System.currentTimeMillis() - 86400000) return FlightValidationResult.InvalidDate("Cannot book flights in the past")

        // 5. Business Rule: Passengers
        if (adults < 1) return FlightValidationResult.InvalidPassengers("At least one adult is required")

        return FlightValidationResult.Success
    }

    sealed interface FlightValidationResult {
        object Success : FlightValidationResult
        data class InvalidOrigin(val reason: String) : FlightValidationResult
        data class InvalidDestination(val reason: String) : FlightValidationResult
        data class InvalidDate(val reason: String) : FlightValidationResult
        data class InvalidPassengers(val reason: String) : FlightValidationResult
        object SameLocation : FlightValidationResult
    }
}