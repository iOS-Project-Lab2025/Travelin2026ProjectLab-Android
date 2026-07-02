package com.softserveacademy.core.presentation.design_system.components.util.inputs


import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height 
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable 
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.VisualTransformation
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.core.presentation.design_system.theme.shapes

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.theme.shapes
/**
 * A custom outlined text field component for the Travelin app.
 *
 * @param value The current text value to be displayed in the input field.
 * @param onValueChange The callback that is triggered when the input text changes.
 * @param placeholder The text to be displayed as a placeholder when the input field is empty.
 * @param modifier The modifier to be applied to the layout.
 * @param state The state of the input field (Normal, Error), which affects its visual style.
 * @param errorMessage An optional error message to be displayed below the input field when in an error state.
 * @param leadingIcon An optional composable to be displayed at the start of the input field.
 * @param trailingIcon An optional composable to be displayed at the end of the input field.
 * @param visualTransformation The transformation to be applied to the input text (e.g., for password masking).
 * @param keyboardOptions Software keyboard options like keyboard type and ImeAction.
 * @param keyboardActions The actions to be performed in response to software keyboard events.
 * @param singleLine Whether the input field should be restricted to a single line of text.
 * @param enabled Whether the input field is enabled for user interaction.
 * @param interactionSource The [MutableInteractionSource] to be used for this input field.
 */
@Composable
fun AppInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    state: AppInputState = AppInputState.Normal,
    errorMessage: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val inputStyle = state.style()

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            placeholder = {
                Text(
                    text = placeholder,
                    color = inputStyle.placeholderColor
                )
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            shape = shape,

            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = inputStyle.textColor,
                unfocusedTextColor = inputStyle.textColor,
                disabledTextColor = inputStyle.textColor,
                focusedContainerColor = inputStyle.containerColor,
                unfocusedContainerColor = inputStyle.containerColor,
                disabledContainerColor = inputStyle.containerColor,
                focusedBorderColor = inputStyle.focusedBorderColor,
                unfocusedBorderColor = inputStyle.borderColor,
                errorBorderColor = inputStyle.borderColor,
                errorContainerColor = inputStyle.containerColor,
                cursorColor = inputStyle.focusedBorderColor
            ),
            isError = state == AppInputState.Error
        )
        if (state == AppInputState.Error && !errorMessage.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))
            Text(
                text = errorMessage,
                color = inputStyle.errorColor ?: MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
}
