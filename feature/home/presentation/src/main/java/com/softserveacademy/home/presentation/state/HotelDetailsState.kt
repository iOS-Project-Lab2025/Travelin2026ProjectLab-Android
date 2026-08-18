package com.softserveacademy.home.presentation.state

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.Poi
import java.io.Serializable

/**
 * State class for the Hotel Detail screen.
 */
data class HotelDetailsState(
    val isLoading: Boolean = false,
    val isPoiLoading: Boolean = false,
    val hotel: Hotel? = null,
    val areaDescription: String? = null,
    val nearbyTransport: List<Poi> = emptyList(),
    val nearbyRestaurants: List<Poi> = emptyList(),
    val errorMessage: String? = null,
    val poiErrorMessage: String? = null,
    val isDescriptionExpanded: Boolean = false,
    val isFavorite: Boolean = false,
    val showAmenitiesDialog: Boolean = false,
    val showFullMap: Boolean = false,
    val showExploreArea: Boolean = false,
) : Serializable
