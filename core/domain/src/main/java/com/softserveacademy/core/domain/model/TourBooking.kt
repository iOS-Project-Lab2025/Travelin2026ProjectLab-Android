package com.softserveacademy.core.domain.model

import java.time.LocalDate

/**
 * Represents a user's tour reservation.
 *
 * A tour booking contains the reservation-specific information associated
 * with a tour, including the scheduled date, number of participants,
 * confirmation details, and current booking status.
 *
 * @property bookingId Unique identifier of the booking.
 * @property tour Tour associated with this booking.
 * @property date Scheduled date of the tour.
 * @property participants Number of participants included in the booking.
 * @property confirmationCode Tour provider or booking confirmation code.
 * @property status Current status of the booking.
 */
data class TourBooking(
    val bookingId: String,

    val tour: Tour,

    val date: LocalDate,

    val participants: Int,

    val confirmationCode: String,

    val status: BookingStatus
)

/**
 * Represents the lifecycle states of a booking.
 *
 * @property CONFIRMED The booking has been confirmed by the provider.
 * @property PENDING The booking is awaiting confirmation.
 * @property CANCELLED The booking has been cancelled.
 * @property COMPLETED The booked service has been completed.
 */
enum class BookingStatus {
    CONFIRMED,
    PENDING,
    CANCELLED,
    COMPLETED
}