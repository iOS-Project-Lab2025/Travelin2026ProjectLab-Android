package com.softserveacademy.core.presentation.design_system.components.util.buttons

import androidx.compose.ui.graphics.Color

/**
 * Defines the colors for a primary button variant.
 * @param containerColor The background color of the button
 * @param contentColor The content color of the button
 * @param borderColor The border color of the button
 */

data class PrimaryButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color? = null,
)