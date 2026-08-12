package com.softserveacademy.feature.booking.flight.domain.model

import com.softserveacademy.core.domain.model.CabinClass
import com.softserveacademy.core.domain.model.FlightSegment
import com.softserveacademy.core.domain.model.FlightType
import kotlinx.serialization.Serializable

/**
 * Represents the temporary state of a flight booking process.
 * Used to persist user selections across different screens.
 */
@Serializable
data class FlightBookingDraft(
    val origin: String = "",
    val destination: String = "",
    val segments: List<FlightSegment> = listOf(FlightSegment()),
    val returnDateMillis: Long? = null, // Only for Round Trip
    val startDateMillis: Long? = null,
    val endDateMillis: Long? = null,
    val adults: Int = 1,
    val children: Int = 0,
    val infants: Int = 0,
    // MAp: segment index -> FlightOffer selected
    val selectedOffers: Map<Int, com.softserveacademy.core.domain.model.FlightOffer> = emptyMap(),
    // this index shows wich segment we are selecting
    val currentSelectingIndex: Int = 0,
    val flightType: FlightType = FlightType.ROUND_TRIP,
    val cabinClass: CabinClass = CabinClass.ECONOMY
)