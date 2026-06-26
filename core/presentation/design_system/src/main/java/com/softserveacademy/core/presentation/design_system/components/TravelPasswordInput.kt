package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInput
import com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInputState
import com.softserveacademy.core.presentation.design_system.theme.EyeIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

@Composable
fun AppPasswordInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    state: AppInputState = AppInputState.Normal,
    errorMessage: String? = null,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    var passwordVisible by remember { mutableStateOf(false) }

    AppInput(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = modifier,
        state = state,
        errorMessage = errorMessage,
        enabled = enabled,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = EyeIcon,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

//---------------------------------------------------------
// PREVIEWS
//---------------------------------------------------------
@Composable
private fun PasswordInputCatalog() {
    Column(
        modifier = Modifier.padding(TravelinDimens.PaddingMedium),
        verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
    ) {
        var passwordNormal by remember { mutableStateOf("") }
        AppPasswordInput(
            value = passwordNormal,
            onValueChange = { passwordNormal = it },
            placeholder = "Password"
        )

        var passwordError by remember { mutableStateOf("123") }
        AppPasswordInput(
            value = passwordError,
            onValueChange = { passwordError = it },
            placeholder = "Confirm Password",
            state = AppInputState.Error,
            errorMessage = "Password must be at least 6 characters"
        )
    }
}

@Preview(name = "Password Input - Light", showBackground = true)
@Composable
private fun AppPasswordInputLightPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            PasswordInputCatalog()
        }
    }
}

@Preview(name = "Password Input - Dark", showBackground = true)
@Composable
private fun AppPasswordInputDarkPreview() {
    Travelin2026ProjectLabTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            PasswordInputCatalog()
        }
    }
}