package com.softserveacademy.feature.booking.flight.presentation.states

import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.domain.model.CabinClass
import com.softserveacademy.core.domain.model.FlightType
import com.softserveacademy.feature.booking.common.presentation.states.TravelEnterBookingDetailsState
import com.softserveacademy.feature.booking.flight.domain.usecase.ValidateFlightSearchUseCase

/**
 * UI State for the Flight Search criteria screen.
 * Captures all user inputs required to perform a flight search.
 *
 * @property segments List of flight tranches (Origin, Destination, Date).
 * @property activeSegmentIndex The index of the segment currently being edited (for autocomplete).
 * @property originSuggestions Matching airports for the current origin query.
 * @property destinationSuggestions Matching airports for the current destination query.
 * @property adults Number of adult passengers (age 12+).
 * @property children Number of child passengers (age 2-12).
 * @property infants Number of infant passengers (under age 2).
 * @property selectedFlightType The mode of travel: ONE_WAY, ROUND_TRIP, or MULTI_CITY.
 * @property selectedCabinClass Preference for travel class (ECONOMY, BUSINESS, etc.).
 * @property errorMessage Resource ID of a global validation or network error.
 * @property errors Map of segment-specific validation errors (keyed by segment index).
 * @property globalDateError Validation error specifically for return dates in Round Trip.
 */
data class FlightSearchState(
    val segments: List<com.softserveacademy.core.domain.model.FlightSegment> = listOf(com.softserveacademy.core.domain.model.FlightSegment()),
    val activeSegmentIndex: Int = 0,
    val originSuggestions: List<Airport> = emptyList(),
    val destinationSuggestions: List<Airport> = emptyList(),
    val adults: Int = 1,
    val children: Int = 0,
    val infants: Int = 0,
    val bookingDetailsState: TravelEnterBookingDetailsState = TravelEnterBookingDetailsState(),
    val errorMessage: Int? = null,
    val selectedFlightType: FlightType = FlightType.ROUND_TRIP,
    val selectedCabinClass: CabinClass = CabinClass.ECONOMY,
    val showCabinSheet: Boolean = false,
    val showDatePicker: Boolean = false,
    val errors: Map<Int, ValidateFlightSearchUseCase.SegmentError> = emptyMap(),
    val globalDateError: ValidateFlightSearchUseCase.FlightError? = null
)