package com.softserveacademy.core.presentation.design_system.components.util.buttons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Defines the text action button variants:
 * - Neutral: This variant use the onSurface color as the text color.
 * - CallToAction: This variant use the primary color as the text color.
 */
enum class TravelTextActionButtonVariant {
    Neutral,
    CallToAction,
}

@Composable
internal fun TravelTextActionButtonVariant.color(): Color {
    return when (this) {
        TravelTextActionButtonVariant.Neutral -> MaterialTheme.colorScheme.onSurface
        TravelTextActionButtonVariant.CallToAction -> MaterialTheme.colorScheme.primary
    }
}
