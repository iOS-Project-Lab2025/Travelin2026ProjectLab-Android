package com.softserveacademy.core.presentation.design_system.components.util.buttons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
* Defines the primary button variants:
 * - CallToAction This variant use the primary color as the background color.
 * - Neutral: This variant use the surface color as the background color.
 * - SecondaryAction: This variant use the background color as the background color.
*/
enum class TravelPrimaryButtonVariant {
    CallToAction,
    Neutral,
    SecondaryAction,
    BackToHome,
}

/**
 * Returns the colors for the primary button variant.
 * @return The colors for the primary button variant as a [TravelPrimaryButtonColors] object
 */
@Composable
internal fun TravelPrimaryButtonVariant.colors(): TravelPrimaryButtonColors {

    return when (this) {
        TravelPrimaryButtonVariant.CallToAction ->
            TravelPrimaryButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )

        TravelPrimaryButtonVariant.Neutral ->
            TravelPrimaryButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            )

        TravelPrimaryButtonVariant.SecondaryAction ->
            TravelPrimaryButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
            )
        TravelPrimaryButtonVariant.BackToHome ->
            TravelPrimaryButtonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.primary,
            )
    }
}