package com.softserveacademy.feature.booking.flight.domain.model

import com.softserveacademy.core.domain.model.CabinClass
import com.softserveacademy.core.domain.model.FlightContactInfo
import com.softserveacademy.core.domain.model.FlightPassenger
import com.softserveacademy.core.domain.model.FlightSegment
import com.softserveacademy.core.domain.model.FlightType
import kotlinx.serialization.Serializable

/**
 * Represents the temporary state of a flight booking process.
 * Used to persist user selections across different screens (Search, Results, Passengers, Checkout).
 *
 * This model is the single source of truth for the ongoing booking flow.
 *
 * @property origin Initial departure IATA code (redundant but kept for legacy sync).
 * @property destination Final arrival IATA code (redundant but kept for legacy sync).
 * @property segments List of all flight legs included in this search/booking.
 * @property returnDateMillis Specifically for Round Trip mode, the date of the second leg.
 * @property startDateMillis Global departure start time.
 * @property endDateMillis Global return time for Round Trip.
 * @property adults Count of adult passengers.
 * @property children Count of child passengers.
 * @property infants Count of infant passengers.
 * @property selectedOffers Map linking a segment index to a specific [FlightOffer] chosen by the user.
 * @property currentSelectingIndex Progress indicator for the Results screen.
 * @property flightType The mode of travel (ONE_WAY, ROUND_TRIP, MULTI_CITY).
 * @property cabinClass The travel class preference.
 * @property passengers List of all passengers included in this booking.
 * @property contactInfo Contact details for the booking.
 * @property userId Unique identifier of the user making the booking.
 */
@Serializable
data class FlightBookingDraft(
    val origin: String = "",
    val destination: String = "",
    val segments: List<FlightSegment> = listOf(FlightSegment()),
    val returnDateMillis: Long? = null,
    val startDateMillis: Long? = null,
    val endDateMillis: Long? = null,
    val adults: Int = 1,
    val children: Int = 0,
    val infants: Int = 0,
    val selectedOffers: Map<Int, com.softserveacademy.core.domain.model.FlightOffer> = emptyMap(),
    val currentSelectingIndex: Int = 0,
    val flightType: FlightType = FlightType.ROUND_TRIP,
    val cabinClass: CabinClass = CabinClass.ECONOMY,

    val passengers: List<FlightPassenger> = emptyList(),
    val contactInfo: FlightContactInfo? = null,
    val userId: String? = null
)