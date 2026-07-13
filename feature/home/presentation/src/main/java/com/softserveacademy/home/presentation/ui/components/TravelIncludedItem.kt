package com.softserveacademy.home.presentation.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.home.presentation.model.IncludedItemUi

/**
 * A reusable component to display the services included in a hotel package.
 *
 * @param item The [IncludedItemUi] containing title, icon, and optional subtitle.
 * @param modifier The modifier to be applied to the component.
 */
@Composable
fun TravelIncludedItem(
    item: IncludedItemUi,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.small
            )
    ) {
        Row(
            modifier = Modifier
                .padding(TravelinDimens.PaddingNormal),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryFixed,
                modifier = Modifier
                    .size(TravelinDimens.IconSizeSmall)
            )

            Spacer(modifier = Modifier.width(TravelinDimens.SpaceSmall))

            Column {
                Text(
                    text = stringResource(id = item.title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryFixed
                )
                item.subtitle?.let {
                    Text(
                        text = stringResource(id = it),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TravelHotelDetailScreenPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        TravelIncludedItem(IncludedItemUi.BuffetBreakfast)
    }
}
