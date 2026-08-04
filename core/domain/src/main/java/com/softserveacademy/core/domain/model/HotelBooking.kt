package com.softserveacademy.core.domain.model

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
    val bookingId: String,
    val hotelId: String,
    val roomId: String,
    val checkIn: Long,
    val checkOut: Long,
    val guests: BookingGuests,
    val price: BookingPrice,
    val status: BookingStatus,
    val confirmationCode: String,
    val createdAt: Long,
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
 * @property roomPricePerNight The price of the room per night.
 * @property roomPrice The total price for the room stay.
 * @property taxes Total taxes applied to the booking.
 * @property fees Total fees applied to the booking.
 * @property total The final total price including taxes and fees.
 */
@Serializable
data class BookingPrice(
    val roomPricePerNight: Int,
    val roomPrice: Int,
    val taxes: Int = 0,
    val fees: Int = 0,
    val total: Int
)

/**
 * Data class representing the contact information for a booking.
 *
 *  @property firstName The first name of the guest.
 *  @property lastName The last name of the guest.
 *  @property email The email address of the guest.
 *  @property countryCode The country code of the guest's phone number.
 *  @property phoneNumber The phone number of the guest.
 */
@Serializable
data class BookingContactInfo(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val countryCode: String = "",
    val phoneNumber: String = ""
)
