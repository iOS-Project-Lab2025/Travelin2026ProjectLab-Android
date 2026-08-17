package com.softserveacademy.core.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a user's flight reservation.
 *
 * A flight booking contains one or more flights and tickets under a single
 * confirmation code (PNR). For example, a round-trip booking with 3 passengers
 * would have 2 flights and 3 tickets.
 *
 * @property bookingId Unique identifier of the booking.
 * @property flights The list of flights included in this booking.
 * @property totalAmount Total price of all tickets in the booking.
 * @property confirmationCode Airline or booking confirmation code (PNR).
 * @property status Current status of the booking.
 * @property currencyCode The ISO 4217 currency code for the booking.
 * @property contactInfo Contact information associated with the booking.
 */

@kotlinx.serialization.Serializable
data class FlightBooking(
    val bookingId: String,
    val flights: List<Flight>,
    val tickets: List<Ticket>,
    val confirmationCode: String,
    val status: BookingStatus,
    val totalAmount: Double,
    val currencyCode: String,
    val contactInfo: FlightContactInfo
)

/**
 * Contact details associated with a flight booking.
 * @property email Email address of the contact.
 * @property phone Phone number of the contact.
 * @property countryCode Country code of the contact.
 */
@Serializable
data class FlightContactInfo(
    val email: String = "",
    val phone: String = "",
    val countryCode: String = ""
)

