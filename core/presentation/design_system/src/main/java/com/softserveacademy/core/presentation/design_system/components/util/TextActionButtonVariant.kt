package com.softserveacademy.core.presentation.design_system.components.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Defines the text action button variants:
 * - Neutral: This variant use the onSurface color as the text color.
 * - CallToAction: This variant use the primary color as the text color.
 */
enum class TextActionButtonVariant {
    Neutral,
    CallToAction,
}

@Composable
internal fun TextActionButtonVariant.color(): Color {
    return when (this) {
        TextActionButtonVariant.Neutral -> MaterialTheme.colorScheme.onSurface
        TextActionButtonVariant.CallToAction -> MaterialTheme.colorScheme.primary
    }
}