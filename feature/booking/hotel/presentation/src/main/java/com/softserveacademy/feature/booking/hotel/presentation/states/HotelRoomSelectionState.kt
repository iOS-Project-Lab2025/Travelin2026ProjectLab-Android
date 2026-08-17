package com.softserveacademy.feature.booking.hotel.presentation.states

import com.softserveacademy.core.domain.model.HotelRoom
import java.io.Serializable

enum class RoomFilter : Serializable {
    AVAILABLE, ALL, ONE_BED, TWO_BEDS
}

/**
 * Data class representing the state of the hotel room selection screen.
 *
 * @property rooms The list of all rooms that fit the selected criteria.
 * @property availableRoomIds The IDs of rooms that are available for the selected dates.
 * @property filteredRooms The list of rooms after applying filters.
 * @property selectedRoomId The ID of the currently selected room.
 * @property selectedFilter The currently active filter (e.g., "Available", "All", "1 Bed").
 * @property isLoading Whether the room data is being fetched.
 * @property error The error message to display if loading fails.
 * @property nightCount The number of nights for the booking.
 */
data class HotelRoomSelectionState(
    val rooms: List<HotelRoom> = emptyList(),
    val availableRoomIds: Set<String> = emptySet(),
    val filteredRooms: List<HotelRoom> = emptyList(),
    val selectedRoomId: String? = null,
    val selectedFilter: RoomFilter = RoomFilter.AVAILABLE,
    val isLoading: Boolean = false,
    val error: String? = null,
    val nightCount: Int = 1
) : Serializable
