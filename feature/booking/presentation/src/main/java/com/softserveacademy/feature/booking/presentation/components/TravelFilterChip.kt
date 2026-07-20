package com.softserveacademy.feature.booking.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.presentation.R

/**
 * A custom filter chip composable that represents a single filter option in the UI.
 *
 * @param text The text to display on the chip.
 * @param isSelected Whether the chip is currently selected.
 * @param onClick Callback when the chip is clicked.
 * @param modifier The modifier to be applied to the chip.
 */
@Composable
fun TravelFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .selectable(selected = isSelected, onClick = onClick),
        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent,
        border = BorderStroke(
            2.dp,
            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HotelRoomSelectionScreenPreview() {
    Travelin2026ProjectLabTheme {
        Row(
            modifier = Modifier.padding(TravelinDimens.PaddingSmall),
            horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
        ) {
            TravelFilterChip(
                text = stringResource(R.string.filter_available),
                isSelected = true,
                onClick = {}
            )
            TravelFilterChip(
                text = stringResource(R.string.filter_all),
                isSelected = false,
                onClick = {}
            )
        }
    }
}