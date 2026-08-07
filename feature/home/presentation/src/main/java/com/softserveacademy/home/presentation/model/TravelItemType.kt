package com.softserveacademy.home.presentation.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

/**
 * Enum representing the types of travel items supported.
 */

@Keep
@Serializable
enum class TravelItemType {
    HOTEL,
    TOUR
}
