package com.softserveacademy.core.presentation.design_system.components.util.buttons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
* Defines the auth primary button variants:
* - Color Background: This variant use the primary color as the background color.
* - Color Content: This variant use the primary color as the content color.
*/
enum class TravelAuthPrimaryButtonVariant {
    ColorBackground,
    ColorContent,
}

/**
 * Returns the colors for the auth primary button variant.
 * @return The colors for the auth primary button variant as a [TravelPrimaryButtonColors] object
 */
@Composable
internal fun TravelAuthPrimaryButtonVariant.colors(): TravelPrimaryButtonColors {

    return when (this) {
        TravelAuthPrimaryButtonVariant.ColorBackground ->
            TravelPrimaryButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )

        TravelAuthPrimaryButtonVariant.ColorContent ->
            TravelPrimaryButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
            )
    }
}