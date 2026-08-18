package com.softserveacademy.feature.booking.tour.domain.model

import com.softserveacademy.core.domain.model.BookingContactInfo
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
    val contactInfo: BookingContactInfo = BookingContactInfo()
) : JavaSerializable

/**
 * Data class representing the participant information for a booking.
 *
 * @property adults The number of adult participants.
 * @property children The number of child participants (2-17 years old).
 * @property infants The number of infant participants (0-2 years old).
 */
@Serializable
data class Participants(
    val adults: Int = 1,
    val children: Int = 0,
    val infants: Int = 0
) : JavaSerializable
