package com.softserveacademy.core.presentation.design_system.components.util.buttons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
* Defines the primary button variants:
 * - CallToAction This variant use the primary color as the background color.
 * - Neutral: This variant use the surface color as the background color.
 * - SecondaryAction: This variant use the background color as the background color.
*/
enum class PrimaryButtonVariant {
    CallToAction,
    Neutral,
    SecondaryAction,
}

/**
 * Returns the colors for the primary button variant.
 * @return The colors for the primary button variant as a [PrimaryButtonColors] object
 */
@Composable
internal fun PrimaryButtonVariant.colors(): PrimaryButtonColors {

    return when (this) {
        PrimaryButtonVariant.CallToAction ->
            PrimaryButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )

        PrimaryButtonVariant.Neutral ->
            PrimaryButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            )

        PrimaryButtonVariant.SecondaryAction ->
            PrimaryButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
            )
    }
}