package com.softserveacademy.home.presentation.state

import com.softserveacademy.core.domain.model.Tour
import java.io.Serializable

/**
 * State class for the Tour Detail screen.
 */
data class TourDetailsState(
    val isLoading: Boolean = false,
    val tourDetails: Tour? = null,
    val errorMessage: String? = null,
    val isDescriptionExpanded: Boolean = false,
    val isFavorite: Boolean = false,
    val showFullMap: Boolean = false,
    val showAllAmenities: Boolean = false
) : Serializable