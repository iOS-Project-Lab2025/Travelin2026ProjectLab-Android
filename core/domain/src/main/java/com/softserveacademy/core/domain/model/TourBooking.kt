package com.softserveacademy.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a user's tour reservation.
 *
 * A tour booking contains the reservation-specific information associated
 * with a tour, including the scheduled date, number of participants,
 * confirmation details, and current booking status.
 *
 * @property bookingId Unique identifier of the booking.
 * @property tourId Unique identifier of the tour associated with this booking.
 * @property startDate Scheduled start date (epoch millis).
 * @property endDate Scheduled end date (epoch millis).
 * @property participants Granular information about the participants in the booking.
 * @property status Current status of the booking.
 * @property confirmationCode Tour provider or booking confirmation code.
 * @property createdAt Timestamp when the booking order was created.
 * @property contactInfo The guest contact information for the booking.
 */
@Serializable
data class TourBooking(
    @SerialName("booking_id")
    val bookingId: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("tour_id")
    val tourId: String,
    @SerialName("start_date")
    val startDate: Long,
    @SerialName("end_date")
    val endDate: Long,
    val participants: BookingParticipants,
    val price: TourBookingPrice,
    val status: BookingStatus,
    @SerialName("confirmation_code")
    val confirmationCode: String,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("contact_info")
    val contactInfo: BookingContactInfo? = null
)

/**
 * Data class representing the participant information for a booking.
 *
 * @property adults The number of adult participants.
 * @property children The number of child participants (2-17 years old).
 * @property infants The number of infant participants (0-2 years old).
 */
@Serializable
data class BookingParticipants(
    val adults: Int = 1,
    val children: Int = 0,
    val infants: Int = 0
)

/**
 * Data class representing the detailed price breakdown for a booking.
 *
 * @property ratePerAdult The price of the tour per adult.
 * @property ratePerChildren The price of the tour per children.
 * @property ratePerInfant The price of the tour per infant.
 * @property taxes Total taxes applied to the booking.
 * @property fees Total fees applied to the booking.
 * @property total The final total price including taxes and fees.
 */
@Serializable
data class TourBookingPrice(
    @SerialName("rate_per_adult")
    val ratePerAdult: Double,
    @SerialName("rate_per_children")
    val ratePerChildren: Double,
    @SerialName("rate_per_infant")
    val ratePerInfant: Double,
    val subtotal: Int,
    val taxes: Int = 0,
    val fees: Int = 0,
    val total: Int
)