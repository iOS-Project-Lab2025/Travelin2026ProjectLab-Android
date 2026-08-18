package com.softserveacademy.core.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents an individual issued e-ticket for a passenger on a specific flight leg.
 *
 * @property ticketNumber Unique identifier for the electronic ticket (e.g., TK-12345).
 * @property passengerName Full name of the traveler as it appears on the document.
 * @property flightId Reference to the unique ID of the associated flight.
 * @property flightNumber Commercial flight number (e.g., LA500).
 * @property originCode IATA code of the departure airport for this leg (e.g., SCL).
 * @property destinationCode IATA code of the arrival airport for this leg (e.g., JFK).
 * @property seatNumber Assigned seat (optional until check-in).
 * @property gate Assigned boarding gate (optional).
 * @property boardingGroup Group for boarding sequence (optional).
 * @property cabinClass Service level for this ticket.
 */
@Serializable
data class Ticket(
    val ticketNumber: String,
    val passengerName: String,
    val flightId: String,
    val flightNumber: String,
    val originCode: String,
    val destinationCode: String,
    val seatNumber: String? = null,
    val gate: String? = null,
    val boardingGroup: String? = null,
    val cabinClass: CabinClass = CabinClass.ECONOMY
)