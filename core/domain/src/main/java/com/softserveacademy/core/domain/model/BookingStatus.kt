package com.softserveacademy.core.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the lifecycle states of a booking.
 *
 * @property CREATED User confirmed booking and went to payment.
 * @property PENDING The booking is awaiting payment.
 * @property CANCELLED Something went wrong during the booking process.
 * @property COMPLETED Booking was successful.
 */
@Serializable
enum class BookingStatus {
    CREATED,
    PENDING,
    CANCELLED,
    COMPLETED
}