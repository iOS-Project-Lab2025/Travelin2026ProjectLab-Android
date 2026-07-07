package com.softserveacademy.core.presentation.design_system.components.util.buttons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
* Defines the auth primary button variants:
* - Color Background: This variant use the primary color as the background color.
* - Color Content: This variant use the primary color as the content color.
*/
enum class AuthPrimaryButtonVariant {
    ColorBackground,
    ColorContent,
}

/**
 * Returns the colors for the auth primary button variant.
 * @return The colors for the auth primary button variant as a [PrimaryButtonColors] object
 */
@Composable
internal fun AuthPrimaryButtonVariant.colors(): PrimaryButtonColors {

    return when (this) {
        AuthPrimaryButtonVariant.ColorBackground ->
            PrimaryButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )

        AuthPrimaryButtonVariant.ColorContent ->
            PrimaryButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
            )
    }
}