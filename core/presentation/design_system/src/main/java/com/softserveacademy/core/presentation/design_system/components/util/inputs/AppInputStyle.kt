package com.softserveacademy.core.presentation.design_system.components.util.inputs
import androidx.compose.ui.graphics.Color
/**
 * Data class defining the visual properties of an [AppInput].
 *
 * @property textColor The color of the input text.
 * @property placeholderColor The color of the placeholder text.
 * @property borderColor The color of the border when not focused.
 * @property containerColor The background color of the input field.
 * @property focusedBorderColor The color of the border when the field is focused.
 * @property errorColor The color used for error messages and states.
 */
data class AppInputStyle(
    val textColor: Color,
    val placeholderColor: Color,
    val borderColor: Color,
    val containerColor: Color,
    val focusedBorderColor: Color,
    val errorColor: Color? = null,
)
