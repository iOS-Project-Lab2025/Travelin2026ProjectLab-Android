package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.util.TextActionButtonVariant
import com.softserveacademy.core.presentation.design_system.components.util.color
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * Clickable text that performs an action.
 *
 * @param text The text to display as clickable
 * @param onClick The action to perform when the button is clicked
 * @param modifier The modifier to apply to the button
 * @param enabled Whether the button is enabled or disabled
 * @param variant The variant of the button (`TextActionButtonVariant.Neutral`, `TextActionButtonVariant.CallToAction`)
 */
@Composable
fun TravelTextActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: TextActionButtonVariant = TextActionButtonVariant.Neutral,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val alpha by animateFloatAsState(
        targetValue = when {
            !enabled -> 0.3f
            isPressed -> 0.5f
            else -> 1f
        }
    )
    Text(
        text = text,
        color = variant.color(),
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier
            .alpha(alpha)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    )
}

@Preview(showBackground = true)
@Composable
fun TravelTextActionButtonPreview() {
    Travelin2026ProjectLabTheme {
        Column(
            modifier = Modifier.padding(TravelinDimens.PaddingSmall),
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall)
        ) {
            TravelTextActionButton(
                text = "Press here",
                onClick = {},
                modifier = Modifier
            )

            TravelTextActionButton(
                text = "Disabled button",
                onClick = {},
                enabled = false,
                modifier = Modifier
            )

            TravelTextActionButton(
                text = "Press here",
                onClick = {},
                modifier = Modifier,
                variant = TextActionButtonVariant.CallToAction
            )
            TravelTextActionButton(
                text = "Disabled button",
                onClick = {},
                enabled = false,
                modifier = Modifier,
                variant = TextActionButtonVariant.CallToAction
            )
        }
    }
}