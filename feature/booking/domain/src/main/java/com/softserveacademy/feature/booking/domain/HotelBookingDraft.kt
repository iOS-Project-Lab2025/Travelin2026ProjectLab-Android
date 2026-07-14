package com.softserveacademy.feature.booking.domain

import java.io.Serializable

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
 * @property guest The guest information for the booking.
 */
data class HotelBookingDraft(
    val hotelId: String? = null,
    val roomId: String? = null,
    val checkInDate: Long? = null,
    val checkOutDate: Long? = null,
    val amountOfAdults: Int = 1,
    val amountOfChildren: Int = 0,
    val hasPets: Boolean = false,
    val guest: GuestInfo = GuestInfo()
) : Serializable

/**
 * Data class representing the guest information for a booking.
 *
 * @property firstName The first name of the guest.
 * @property lastName The last name of the guest.
 * @property email The email address of the guest.
 * @property phoneNumber The phone number of the guest.
 */
data class GuestInfo(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = ""
) : Serializable
