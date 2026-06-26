package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInput
import com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInputState
import com.softserveacademy.core.presentation.design_system.theme.SearchIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

@Composable
fun AppTextInput(
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
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Sentences,
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
private fun TextInputCatalog() {
    Column(
        modifier = Modifier.padding(TravelinDimens.PaddingMedium),
        verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
    ) {
        var name by remember { mutableStateOf("") }
        AppTextInput(
            value = name,
            onValueChange = { name = it },
            placeholder = "First name"
        )

        var search by remember { mutableStateOf("Barcelona") }
        AppTextInput(
            value = search,
            onValueChange = { search = it },
            placeholder = "Where to go?",
            leadingIcon = {
                Icon(
                    imageVector = SearchIcon,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
    }
}

@Preview(name = "Text Input - Light", showBackground = true)
@Composable
private fun AppTextInputLightPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            TextInputCatalog()
        }
    }
}

@Preview(name = "Text Input - Dark", showBackground = true)
@Composable
private fun AppTextInputDarkPreview() {
    Travelin2026ProjectLabTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            TextInputCatalog()
        }
    }
}