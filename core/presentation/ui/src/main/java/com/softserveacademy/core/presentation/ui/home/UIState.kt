package com.softserveacademy.core.presentation.ui.home

import com.softserveacademy.core.domain.model.Hotel

/**
 * Sealed class representing the state of a hotel card.
 *
 * - Error: Represents an error state with an optional error message.
 * - Data: Represents a data state with a hotel object.
 * - IsLoading: Represents a loading state with an optional loading state.
 */
sealed class UIHotelCardState(){
    data class Error(val message: String?): UIHotelCardState()
    data class Data(val hotel: Hotel): UIHotelCardState()
    data class IsLoading(val state: Boolean = false): UIHotelCardState()
}