package com.softserveacademy.home.presentation.model

import kotlinx.serialization.Serializable

/**
 * Enum representing the types of travel items supported.
 */
@Serializable
enum class TravelItemType {
    HOTEL,
    TOUR
}
