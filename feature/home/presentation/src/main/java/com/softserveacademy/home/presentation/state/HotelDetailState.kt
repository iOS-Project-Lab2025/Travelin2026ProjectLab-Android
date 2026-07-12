package com.softserveacademy.home.presentation.state

import com.softserveacademy.core.domain.model.HotelDetails

/**
 * Represents the various states of the Hotel Detail screen.
 *
 * This sealed class is used to manage the UI state in a predictable way,
 * allowing the UI to react to different scenarios like loading, success, or error.
 */
sealed class HotelDetailState {

    data class Error(val message: String?) : HotelDetailState()
    data class Data(val hotelDetail: HotelDetails) : HotelDetailState()
    data class IsLoading(val state: Boolean = false) : HotelDetailState()
}
