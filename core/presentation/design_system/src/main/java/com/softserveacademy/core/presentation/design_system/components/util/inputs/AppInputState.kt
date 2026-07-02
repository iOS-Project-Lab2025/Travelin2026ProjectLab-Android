package com.softserveacademy.core.presentation.design_system.components.util.inputs

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * Represents the different visual and functional states of an [AppInput] component.
 */
enum class AppInputState {
    /** The default state of the input field. */
    Normal,
    /** The state used when there is a validation error. */
    Error
}

/**
 * Extension function to retrieve the [AppInputStyle] associated with the current state.
 */
@Composable
internal fun AppInputState.style(): AppInputStyle {
    val colors = MaterialTheme.colorScheme

    return when (this) {
        AppInputState.Normal -> AppInputStyle(
            textColor = colors.onBackground,
            placeholderColor = colors.onSurfaceVariant,
            containerColor = colors.surface,
            borderColor = colors.outline.copy(alpha = 0.5f),
            focusedBorderColor = colors.primary
        )
        AppInputState.Error -> AppInputStyle(
            textColor = colors.onBackground,
            placeholderColor = colors.onSurfaceVariant,
            containerColor = colors.surface,
            borderColor = colors.error,
            focusedBorderColor = colors.error,
            errorColor = colors.error
        )
    }
}