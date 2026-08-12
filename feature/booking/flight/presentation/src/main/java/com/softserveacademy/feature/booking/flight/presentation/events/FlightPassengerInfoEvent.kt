package com.softserveacademy.feature.booking.flight.presentation.events

import com.softserveacademy.core.domain.model.DocumentType
import com.softserveacademy.core.domain.model.FlightContactInfo
import com.softserveacademy.core.domain.model.FlightPassenger
import com.softserveacademy.core.domain.model.Gender

/**
 * User intents for the Passenger Information screen.
 */
sealed interface FlightPassengerInfoEvent {

    /** Updates text data (names, ID number) for a passenger. */
    data class OnPassengerDataChanged(val index: Int, val passenger: FlightPassenger) : FlightPassengerInfoEvent

    /** Updates selection-based data (Gender, DocType, BirthDate). */
    data class OnPassengerDetailSelected(val index: Int, val passenger: FlightPassenger) : FlightPassengerInfoEvent

    data class OnContactInfoChanged(val contactInfo: FlightContactInfo) : FlightPassengerInfoEvent

    // --- UI Triggers ---
    data class OnShowGenderSheet(val index: Int) : FlightPassengerInfoEvent
    data class OnShowDocTypeSheet(val index: Int) : FlightPassengerInfoEvent
    data class OnShowDatePicker(val index: Int) : FlightPassengerInfoEvent

    object OnDismissSheet : FlightPassengerInfoEvent
    object OnNextClick : FlightPassengerInfoEvent
}