package com.softserveacademy.core.domain.model

/**
 * Represents a travel itinerary for a user.
 *
 * A trip groups all the reservations associated with a journey, including
 * transportation, accommodation, and scheduled activities, as well as the
 * destination and travel dates.
 *
 * @property id Unique identifier of the trip.
 * @property destination Destination associated with the trip.
 * @property startDate Start date of the trip (epoch millis).
 * @property endDate End date of the trip (epoch millis).
 * @property flights Flight bookings included in the trip.
 * @property hotel Hotel booking associated with the trip, if any.
 * @property tours Tour bookings included in the trip, if any.
 */
data class Trip(
    val id: String,
    val destination: Destination,
    val startDate: Long,
    val endDate: Long,

    val flights: List<FlightBooking>,
    val hotel: HotelBooking?,
    val tours: List<TourBooking>?
)