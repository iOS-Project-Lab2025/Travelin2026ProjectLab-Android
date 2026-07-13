package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.softserveacademy.core.presentation.design_system.components.util.buttons.PrimaryButtonVariant
import com.softserveacademy.core.presentation.design_system.components.util.buttons.colors
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * Primary text button.
 *
 * @param text The text to display on the button
 * @param onClick The action to perform when the button is clicked
 * @param modifier The modifier to apply to the button
 * @param enabled Whether the button is enabled or disabled
 * @param variant The variant of the button (`PrimaryButtonVariant.CallToAction`, `PrimaryButtonVariant.Neutral`)
 */
@Composable
fun TravelPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: PrimaryButtonVariant = PrimaryButtonVariant.CallToAction
) {
    val colors = variant.colors()

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.containerColor,
            contentColor = colors.contentColor,
        ),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(
            TravelinDimens.PaddingLarge,
            TravelinDimens.PaddingMedium
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Preview(widthDp = 250)
@Composable
fun TravelPrimaryButtonPreview() {
    Travelin2026ProjectLabTheme {
        Column(
            modifier = Modifier.padding(TravelinDimens.PaddingSmall),
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
        ) {
            TravelPrimaryButton(
                text = "Button",
                onClick = {},
                variant = PrimaryButtonVariant.CallToAction,
                enabled = true
            )
            TravelPrimaryButton(
                text = "Button",
                onClick = {},
                variant = PrimaryButtonVariant.Neutral,
                enabled = true
            )
            TravelPrimaryButton(
                text = "Button",
                onClick = {},
                variant = PrimaryButtonVariant.SecondaryAction,
                enabled = true
            )
        }
    }
}