package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.core.presentation.design_system.theme.WifiIcon

/**
 * A small chip-like component used for displaying room details or amenities with an optional icon.
 *
 * @param text The text to display inside the chip.
 * @param icon An optional [ImageVector] to display before the text.
 * @param modifier The modifier to be applied to the chip.
 * @param contentColor The color for the text and icon.
 * @param outlineColor The color for the chip border.
 */
@Composable
fun TravelAmenityChip(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    outlineColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = outlineColor,
                shape = MaterialTheme.shapes.extraSmall
            )
            .padding(
                horizontal = TravelinDimens.PaddingSmall,
                vertical = TravelinDimens.PaddingSmall
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(TravelinDimens.IconSizeExtraSmall),
                tint = contentColor
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TravelAmenityChipPreview() {
    Travelin2026ProjectLabTheme {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TravelAmenityChip(text = "Free Wi-Fi", icon = WifiIcon)
            TravelAmenityChip(
                text = "1-5 persons",
                contentColor = MaterialTheme.colorScheme.secondary,
                outlineColor = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
