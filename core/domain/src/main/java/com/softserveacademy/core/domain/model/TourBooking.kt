package com.softserveacademy.core.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a user's tour reservation.
 *
 * A tour booking contains the reservation-specific information associated
 * with a tour, including the scheduled date, number of participants,
 * confirmation details, and current booking status.
 *
 * @property bookingId Unique identifier of the booking.
 * @property tour Tour associated with this booking.
 * @property date Scheduled date of the tour (epoch millis).
 * @property participants Number of participants included in the booking.
 * @property confirmationCode Tour provider or booking confirmation code.
 * @property status Current status of the booking.
 */
@Serializable
data class TourBooking(
    val bookingId: String,

    val tour: Tour,

    val date: Long,

    val participants: Int,

    val confirmationCode: String,

    val status: BookingStatus,

    val contactInfo: BookingContactInfo? = null
)
