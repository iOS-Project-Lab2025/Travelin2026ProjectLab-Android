package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInput
import com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInputState
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

@Composable
fun AppNumberInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    state: AppInputState = AppInputState.Normal,
    errorMessage: String? = null,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    AppInput(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = modifier,
        state = state,
        errorMessage = errorMessage,
        enabled = enabled,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        singleLine = true
    )
}

//---------------------------------------------------------
// PREVIEWS
//---------------------------------------------------------
@Composable
private fun NumberInputCatalog() {
    Column(
        modifier = Modifier.padding(TravelinDimens.PaddingMedium),
        verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
    ) {
        var age by remember { mutableStateOf("") }
        AppNumberInput(
            value = age,
            onValueChange = { age = it },
            placeholder = "Age"
        )

        var invalidPhone by remember { mutableStateOf("999abc") }
        AppNumberInput(
            value = invalidPhone,
            onValueChange = { invalidPhone = it },
            placeholder = "Phone",
            state = AppInputState.Error,
            errorMessage = "Please enter numbers only"
        )
    }
}

@Preview(name = "Number Input - Light", showBackground = true)
@Composable
private fun AppNumberInputLightPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            NumberInputCatalog()
        }
    }
}

@Preview(name = "Number Input - Dark", showBackground = true)
@Composable
private fun AppNumberInputDarkPreview() {
    Travelin2026ProjectLabTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            NumberInputCatalog()
        }
    }
}