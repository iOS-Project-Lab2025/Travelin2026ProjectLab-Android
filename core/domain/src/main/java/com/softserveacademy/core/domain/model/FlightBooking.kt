package com.softserveacademy.core.domain.model

import kotlinx.serialization.Serializable

/**
 * Master record of a finalized flight reservation.
 *
 * @property bookingId Unique identifier of the booking.
 * @property userId Identifier of the user who owns this booking.
 * @property flights List of flight segments included in the itinerary.
 * @property passengers List of individual travelers.
 * @property tickets Issued e-tickets associated with the reservation.
 * @property confirmationCode Airline Record Locator (PNR).
 * @property status Current lifecycle of the booking (Completed, Canceled).
 * @property totalAmount Final price paid for the reservation.
 * @property currencyCode ISO currency code used for payment.
 * @property contactInfo Lead contact details for communication.
 * @property createdAt Timestamp of issuance.
 */
@Serializable
data class FlightBooking(
    val bookingId: String,
    val userId: String,
    val flights: List<Flight>,
    val passengers: List<FlightPassenger>,
    val tickets: List<Ticket>,
    val confirmationCode: String,
    val status: BookingStatus,
    val totalAmount: Double,
    val currencyCode: String,
    val contactInfo: BookingContactInfo,
    val createdAt: Long
)

/**
 * Data class to handle traveler distribution in a group.
 *
 * @property adults Number of passengers over 12 years.
 * @property children Number of passengers between 2 and 12 years.
 * @property infants Number of passengers under 2 years.
 */
@Serializable
data class PassengerCounts(
    val adults: Int = 1,
    val children: Int = 0,
    val infants: Int = 0
) {
    /**
     * Calculates the total number of travelers in the group.
     */
    val total: Int get() = adults + children + infants
}
