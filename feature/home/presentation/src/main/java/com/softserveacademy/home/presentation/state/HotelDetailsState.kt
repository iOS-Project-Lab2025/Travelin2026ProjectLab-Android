package com.softserveacademy.home.presentation.state

import com.softserveacademy.core.domain.model.Hotel
import java.io.Serializable

/**
 * State class for the Hotel Detail screen.
 */
data class HotelDetailsState(
    val isLoading: Boolean = false,
    val hotel: Hotel? = null,
    val errorMessage: String? = null,
    val isDescriptionExpanded: Boolean = false,
    val isFavorite: Boolean = false,
    val showAmenitiesDialog: Boolean = false,
    val showFullMap: Boolean = false
) : Serializable
