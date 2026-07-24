package com.softserveacademy.feature.booking.hotel.domain.model

import kotlinx.serialization.Serializable
import java.io.Serializable as JavaSerializable

/**
 * Data class representing a draft for a hotel booking.
 *
 * This class is used to store the draft state of a hotel booking.
 * The draft is not the same as the final booking, but rather a temporary
 * state used to persist data across navigation events of the booking flow.
 *
 * @property hotelId The ID of the hotel.
 * @property roomId The ID of the room.
 * @property checkIn The check-in date in milliseconds.
 * @property checkOut The check-out date in milliseconds.
 * @property guests The guests information for the booking (adults, children, pets).
 * @property contactInfo The guest contact information for the booking.
 */
@Serializable
data class HotelBookingDraft(
    val hotelId: String? = null,
    val roomId: String? = null,
    val checkIn: Long? = null,
    val checkOut: Long? = null,
    val guests: Guests = Guests(),
    val contactInfo: ContactInfo = ContactInfo()
) : JavaSerializable

/**
 * Data class representing the guest information for a booking.
 *
 * @property adults The number of adult guests.
 * @property children The number of child guests.
 * @property pets Whether the booking includes pets.
 */
@Serializable
data class Guests(
    val adults: Int = 1,
    val children: Int = 0,
    val pets: Boolean = false
) : JavaSerializable

/**
 * Data class representing the contact information for a booking.
 *
 * @property firstName The first name of the guest.
 * @property lastName The last name of the guest.
 * @property email The email address of the guest.
 * @property countryCode The country code of the guest's phone number.
 * @property phoneNumber The phone number of the guest.
 */
@Serializable
data class ContactInfo(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val countryCode: String = "",
    val phoneNumber: String = ""
) : JavaSerializable
