package com.softserveacademy.core.domain.model

/**
 * Represents a user's flight reservation.
 *
 * A flight booking contains one or more flights and tickets under a single
 * confirmation code (PNR). For example, a round-trip booking with 3 passengers
 * would have 2 flights and 3 tickets.
 *
 * @property bookingId Unique identifier of the booking.
 * @property flights The list of flights included in this booking.
 * @property tickets The list of tickets issued for the passengers.
 * @property confirmationCode Airline or booking confirmation code (PNR).
 * @property status Current status of the booking.
 */
data class FlightBooking(
    val bookingId: String,
    val flights: List<Flight>,
    val tickets: List<Ticket>,
    val confirmationCode: String,
    val status: BookingStatus
)
