package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.softserveacademy.core.presentation.design_system.components.util.buttons.AuthPrimaryButtonVariant
import com.softserveacademy.core.presentation.design_system.components.util.buttons.colors
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * Text button used in auth screens that use the primary color as the background or the content color.
 *
 * @param text The text to display on the button
 * @param onClick The action to perform when the button is clicked
 * @param modifier The modifier to apply to the button
 * @param enabled Whether the button is enabled or disabled
 * @param variant The variant of the button (`AuthPrimaryButtonVariant.ColorBackground`, `AuthPrimaryButtonVariant.ColorContent`)
 */
@Composable
fun TravelAuthPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: AuthPrimaryButtonVariant = AuthPrimaryButtonVariant.ColorBackground
) {
    val colors = variant.colors()

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .requiredHeight(TravelinDimens.ButtonHeightMedium)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.containerColor,
            contentColor = colors.contentColor,
        ),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Preview(widthDp = 250)
@Composable
fun TravelAuthPrimaryButtonPreview() {
    Travelin2026ProjectLabTheme {
        Column(
            modifier = Modifier.padding(TravelinDimens.PaddingSmall),
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
        ) {
            TravelAuthPrimaryButton(
                text = "Button",
                onClick = {},
                variant = AuthPrimaryButtonVariant.ColorBackground,
                enabled = true
            )
            TravelAuthPrimaryButton(
                text = "Button",
                onClick = {},
                variant = AuthPrimaryButtonVariant.ColorContent,
                enabled = true
            )
        }
    }
}