package com.softserveacademy.feature.booking.common.presentation.events

import com.softserveacademy.feature.booking.common.presentation.states.RoomFilter

/**
 * Sealed interface representing UI events for the hotel room selection screen.
 */
sealed interface HotelRoomSelectionEvent {
    data class OnFilterSelected(val filter: RoomFilter) : HotelRoomSelectionEvent
    data class OnRoomSelected(val roomId: Int) : HotelRoomSelectionEvent
    data object OnNextClick : HotelRoomSelectionEvent
    data object OnBackClick : HotelRoomSelectionEvent
}
