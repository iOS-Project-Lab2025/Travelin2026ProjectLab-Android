package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import kotlin.Long

/**
 * Custom Outlined Button used for secondary actions.
 *
 * @param text The text to display on the button.
 * @param onClick The action to perform when the button is clicked.
 * @param modifier The modifier to be applied to the button.
 * @param enabled Whether the button is enabled.
 * @param contentPadding The padding to be applied to the content.
 */
@Composable
fun TravelOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = TravelinDimens.PaddingLarge,
        vertical = TravelinDimens.PaddingSmall
    ),
    debounceInterval: Long = 1000L
) {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    OutlinedButton(
        onClick = {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > debounceInterval) {
                lastClickTime = currentTime
                onClick()
            }
        },
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        contentPadding = contentPadding
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TravelOutlinedButtonPreview() {
    Travelin2026ProjectLabTheme {
        Column(
            modifier = Modifier.padding(TravelinDimens.PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
        ) {
            TravelOutlinedButton(
                text = "Outlined Button",
                onClick = {}
            )
            TravelOutlinedButton(
                text = "Disabled Button",
                onClick = {},
                enabled = false
            )
        }
    }
}
