package com.softserveacademy.feature.booking.hotel.presentation.states

import com.softserveacademy.core.domain.model.HotelRoom

enum class RoomFilter {
    AVAILABLE, ALL, ONE_BED, TWO_BEDS
}

/**
 * Data class representing the state of the hotel room selection screen.
 *
 * @property rooms The list of all available rooms for the hotel.
 * @property filteredRooms The list of rooms after applying filters.
 * @property selectedRoomId The ID of the currently selected room.
 * @property selectedFilter The currently active filter (e.g., "Available", "All", "1 Bed").
 * @property isLoading Whether the room data is being fetched.
 * @property nightCount The number of nights for the booking.
 */
data class HotelRoomSelectionState(
    val rooms: List<HotelRoom> = emptyList(),
    val filteredRooms: List<HotelRoom> = emptyList(),
    val selectedRoomId: Int? = null,
    val selectedFilter: RoomFilter = RoomFilter.AVAILABLE,
    val isLoading: Boolean = false,
    val nightCount: Int = 1
)
