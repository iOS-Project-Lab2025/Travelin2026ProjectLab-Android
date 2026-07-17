package com.softserveacademy.core.domain.model

/**
 * Represents a user's flight reservation.
 *
 * A flight booking contains the reservation-specific information associated
 * with a flight, such as the ticket number, assigned seat, boarding details,
 * and current booking status.
 *
 * @property bookingId Unique identifier of the booking.
 * @property flight Flight associated with this booking.
 * @property ticketNumber Electronic ticket number issued for the passenger.
 * @property confirmationCode Airline or booking confirmation code (PNR).
 * @property seat Assigned seat number, if available.
 * @property gate Departure gate, if assigned.
 * @property boardingGroup Passenger boarding group, if applicable.
 * @property status Current status of the booking.
 */
data class FlightBooking(
    val bookingId: String,
    val flight: Flight,

    val ticketNumber: String,
    val confirmationCode: String,

    val seat: String?,
    val gate: String?,
    val boardingGroup: String?,

    val status: BookingStatus
)