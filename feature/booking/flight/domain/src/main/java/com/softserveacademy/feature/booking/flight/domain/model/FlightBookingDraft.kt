package com.softserveacademy.feature.booking.flight.domain.model

import com.softserveacademy.core.domain.model.*
import kotlinx.serialization.Serializable

/**
 * Represents the temporary state of a flight booking process.
 * Acts as the source of truth from search initiation to final checkout.
 *
 * @property userId Unique identifier of the user making the booking.
 * @property flightType The mode of travel (ONE_WAY, ROUND_TRIP, MULTI_CITY).
 * @property passengerCounts Distribution of travelers (Adults, Children, Infants).
 * @property cabinClass Preference for travel class (Economy, Business, etc.).
 * @property segments Itinerary definition. Dates and locations live here.
 * @property selectedOffers User's choice for each segment of the itinerary.
 * @property passengers Full traveler details collected during the checkout wizard.
 * @property contactInfo Lead contact information for the reservation.
 * @property currentSelectingIndex Progress tracker for the multistep result selection.
 */
@Serializable
data class FlightBookingDraft(

    val userId: String? = null,
    val flightType: FlightType = FlightType.ROUND_TRIP,
    val passengerCounts: PassengerCounts = PassengerCounts(),
    val cabinClass: CabinClass = CabinClass.ECONOMY,

    // only source of truth for flight segments. (origin, destination, date)
    val segments: List<FlightSegment> = listOf(FlightSegment()),

    // flight results mapped by segment index.
    val selectedOffers: Map<Int, FlightOffer> = emptyMap(),

    // Checkout data
    val passengers: List<FlightPassenger> = emptyList(),
    val contactInfo: BookingContactInfo? = null,

    val currentSelectingIndex: Int = 0
)