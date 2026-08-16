package com.softserveacademy.feature.booking.flight.presentation.states

import com.softserveacademy.core.domain.model.FlightContactInfo
import com.softserveacademy.core.domain.model.FlightPassenger
import com.softserveacademy.feature.booking.flight.domain.model.ContactError
import com.softserveacademy.feature.booking.flight.domain.model.PassengerError

/**
 * UI State for the Passenger Information screen.
 *
 * @property passengers List of individual travelers and their data.
 * @property contactInfo Primary contact for the reservation.
 * @property isLoading Initial loading state from draft.
 * @property passengerErrors Map of validation errors per passenger index.
 * @property contactError Validation error for contact fields.
 */
data class FlightPassengerInfoState(
    val passengers: List<FlightPassenger> = emptyList(),
    val contactInfo: FlightContactInfo = FlightContactInfo(),
    val isLoading: Boolean = true,
    val passengerErrors: Map<Int, PassengerError> = emptyMap(),
    val contactError: ContactError? = null,
    val showGenderSheet: Boolean = false,
    val showNationalitySheet: Boolean = false,
    val showDocTypeSheet: Boolean = false,
    val showDatePicker: Boolean = false,
    val activePassengerIndex: Int = 0,
    val currentPassengerIndex: Int = 0,
    val isContactStep: Boolean = false,
    val usePrimaryContact: Boolean = false
)