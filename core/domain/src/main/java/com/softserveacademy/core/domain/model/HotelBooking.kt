package com.softserveacademy.core.domain.model

/**
 * Represents a user's hotel reservation.
 *
 * A hotel booking contains reservation-specific information such as the
 * selected room type, stay dates, number of guests, and booking confirmation.
 *
 * @property bookingId Unique identifier of the booking.
 * @property hotel Hotel associated with this booking.
 * @property roomType Type of room reserved (e.g. "Standard", "Deluxe", "Suite").
 * @property checkIn Scheduled check-in date (epoch millis).
 * @property checkOut Scheduled check-out date (epoch millis).
 * @property guests Number of guests included in the reservation.
 * @property confirmationCode Hotel or provider confirmation code.
 * @property status Current status of the booking.
 */
data class HotelBooking(
    val bookingId: String,
    val hotel: Hotel,
    val roomType: String,
    val checkIn: Long,
    val checkOut: Long,
    val guests: Int,
    val confirmationCode: String,
    val status: BookingStatus
)