package com.softserveacademy.core.presentation.design_system.model

/**
 * A generic UI model representing a feature or amenity.
 *
 * @property iconRes The drawable resource ID for the icon.
 * @property labelRes The string resource ID for the display label.
 */
data class FeatureUi(
    val iconRes: Int,
    val labelRes: Int
)