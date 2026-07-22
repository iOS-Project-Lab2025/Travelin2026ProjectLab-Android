package com.softserveacademy.feature.booking.common.domain.model

import kotlinx.serialization.Serializable

/**
 * Data class representing a draft for a hotel booking.
 *
 * @property hotelId The ID of the hotel.
 * @property roomId The ID of the room.
 * @property checkInDate The check-in date in milliseconds.
 * @property checkOutDate The check-out date in milliseconds.
 * @property amountOfAdults The number of adults for the booking.
 * @property amountOfChildren The number of children for the booking.
 * @property hasPets Whether the booking includes pets.
 * @property contactInfo The contact information for the booking.
 */
@Serializable
data class HotelBookingDraft(
    val hotelId: String? = null,
    val roomId: String? = null,
    val checkInDate: Long? = null,
    val checkOutDate: Long? = null,
    val amountOfAdults: Int = 1,
    val amountOfChildren: Int = 0,
    val hasPets: Boolean = false,
    val contactInfo: ContactInfo = ContactInfo()
) : java.io.Serializable

/**
 * Data class representing the contact information for a booking.
 *
 * @property firstName The first name of the guest.
 * @property lastName The last name of the guest.
 * @property email The email address of the guest.
 * @property phoneNumber The phone number of the guest.
 */
@Serializable
data class ContactInfo(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = ""
) : java.io.Serializable
