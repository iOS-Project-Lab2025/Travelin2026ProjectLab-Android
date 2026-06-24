package com.softserveacademy.core.presentation.design_system.components.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
* Defines the primary button variants:
* - Color Background: This variant use the primary color as the background color.
* - Color Content: This variant use the primary color as the content color.
*/
enum class PrimaryButtonVariant {
    ColorBackground,
    ColorContent,
}

@Composable
internal fun PrimaryButtonVariant.colors(): PrimaryButtonColors {

    return when (this) {
        PrimaryButtonVariant.ColorBackground ->
            PrimaryButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )

        PrimaryButtonVariant.ColorContent ->
            PrimaryButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
            )
    }
}