package com.softserveacademy.core.domain.model

import kotlin.time.Duration

/**
 * Represents a commercial flight between two airports.
 *
 * A flight contains scheduling information, origin and destination airports,
 * the operating airline, and cabin class information.
 *
 * @property id Unique identifier of the flight.
 * @property airline Airline operating the flight.
 * @property flightNumber Flight number assigned by the airline.
 * @property origin Departure airport.
 * @property destination Arrival airport.
 * @property departureTime Scheduled departure date and time (epoch millis).
 * @property arrivalTime Scheduled arrival date and time (epoch millis).
 * @property duration Total flight duration.
 * @property cabinClass Travel cabin class.
 */
data class Flight(
    val id: String,
    val airline: Airline,
    val flightNumber: String,

    val origin: Airport,
    val destination: Airport,

    val departureTime: Long,
    val arrivalTime: Long,

    val duration: Duration,

    val cabinClass: CabinClass
)

/**
 * Represents an airline operating commercial flights.
 *
 * @property code Official IATA airline code (e.g. "LA", "AA").
 * @property name Airline name.
 * @property logoUrl URL of the airline logo.
 */
data class Airline(
    val code: String,
    val name: String,
    val logoUrl: String
)

/**
 * Represents an airport used as the origin or destination of a flight.
 *
 * @property code IATA airport code (e.g. "SCL", "JFK").
 * @property name Airport name.
 * @property city City where the airport is located.
 * @property country Country where the airport is located.
 */
data class Airport(
    val code: String,      // SCL
    val name: String,      // Arturo Merino Benítez
    val city: String,
    val country: String
)

/**
 * Represents the available travel cabin classes for a flight.
 */
enum class CabinClass {
    ECONOMY,
    PREMIUM_ECONOMY,
    BUSINESS,
    FIRST
}