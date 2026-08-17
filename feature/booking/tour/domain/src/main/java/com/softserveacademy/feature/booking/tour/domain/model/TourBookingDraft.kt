package com.softserveacademy.feature.booking.tour.domain.model

import kotlinx.serialization.Serializable
import java.io.Serializable as JavaSerializable

/**
 * Data class representing a draft for a tour booking.
 */
@Serializable
data class TourBookingDraft(
    val tourId: String? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val participants: Participants = Participants(),
    val contactInfo: ContactInfo = ContactInfo()
) : JavaSerializable

/**
 * Data class representing the participant information for a booking.
 */
@Serializable
data class Participants(
    val adults: Int = 1,
    val children: Int = 0,
    val infants: Int = 0
) : JavaSerializable

/**
 * Data class representing the contact information for a booking.
 */
@Serializable
data class ContactInfo(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val countryCode: String = "",
    val phoneNumber: String = ""
) : JavaSerializable
