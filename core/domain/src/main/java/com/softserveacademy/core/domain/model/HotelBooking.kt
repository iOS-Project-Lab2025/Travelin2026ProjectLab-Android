package com.softserveacademy.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a user's hotel reservation.
 *
 * A hotel booking contains reservation-specific information such as the
 * selected room, stay dates, number of guests, and booking confirmation.
 *
 * @property bookingId Unique identifier of the booking.
 * @property hotelId Identifier of the hotel associated with this booking.
 * @property roomId Identifier of the room reserved.
 * @property checkIn Scheduled check-in date (epoch millis).
 * @property checkOut Scheduled check-out date (epoch millis).
 * @property guests Granular information about guests included in the reservation.
 * @property price Detailed price breakdown for the booking.
 * @property status Current status of the booking.
 * @property confirmationCode Hotel or provider confirmation code.
 * @property createdAt Timestamp when the booking order was created.
 * @property contactInfo The guest contact information for the booking.
 */
@Serializable
data class HotelBooking(
    @SerialName("booking_id")
    val bookingId: String,
    val userId: String? = null,
    @SerialName("hotel_id")
    val hotelId: String,
    @SerialName("room_id")
    val roomId: String,
    @SerialName("check_in")
    val checkIn: Long,
    @SerialName("check_out")
    val checkOut: Long,
    val guests: BookingGuests,
    val price: BookingPrice,
    val status: BookingStatus,
    @SerialName("confirmation_code")
    val confirmationCode: String,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("contact_info")
    val contactInfo: BookingContactInfo? = null
)

/**
 * Data class representing the guest information for a booking.
 *
 * @property adults The number of adult guests.
 * @property children The number of child guests.
 * @property pets Whether the booking includes pets.
 */
@Serializable
data class BookingGuests(
    val adults: Int = 1,
    val children: Int = 0,
    val pets: Boolean = false
)

/**
 * Data class representing the detailed price breakdown for a booking.
 *
 * @property ratePerNight The price of the room per night.
 * @property roomSubtotal The total price for the room stay.
 * @property taxes Total taxes applied to the booking.
 * @property fees Total fees applied to the booking.
 * @property total The final total price including taxes and fees.
 */
@Serializable
data class BookingPrice(
    @SerialName("rate_per_night")
    val ratePerNight: Int,
    @SerialName("room_subtotal")
    val roomSubtotal: Int,
    val taxes: Int = 0,
    val fees: Int = 0,
    val total: Int
)
