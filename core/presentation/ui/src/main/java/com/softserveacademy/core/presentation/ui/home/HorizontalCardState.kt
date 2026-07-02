package com.softserveacademy.core.presentation.ui.home

import com.softserveacademy.core.domain.model.Hotel

/**
 * Sealed class representing the state of a horizontal card.
 *
 * - Error: Represents an error state with an optional error message.
 * - Data: Represents a data state with a hotel object.
 * - IsLoading: Represents a loading state with an optional loading state.
 */
sealed class HorizontalCardState(){
    data class Error(val message: String?): HorizontalCardState()
    data class Data(val hotel: Hotel): HorizontalCardState()
    data class IsLoading(val state: Boolean = false): HorizontalCardState()
}