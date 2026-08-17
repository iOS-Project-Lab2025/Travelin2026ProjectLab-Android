package com.softserveacademy.core.presentation.design_system.components

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.components.util.mapToFeature
import com.softserveacademy.core.presentation.design_system.model.FeatureUi
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * A reusable component to display the features included in a hotel package or tour.
 *
 * @param feature The [FeatureUi] containing localized label and icon.
 * @param modifier The modifier to be applied to the component.
 */
@Composable
fun TravelFeatureChip(
    feature: FeatureUi,
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
                painter = painterResource(id = feature.iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryFixed,
                modifier = Modifier
                    .size(TravelinDimens.IconSizeSmall)
            )

            Spacer(modifier = Modifier.width(TravelinDimens.SpaceSmall))

            Column {
                Text(
                    text = stringResource(id = feature.labelRes),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryFixed
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TravelFeatureChipPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        val sampleFeature = mapToFeature("hotel.breakfast")
        if (sampleFeature != null) {
            TravelFeatureChip(sampleFeature)
        }
    }
}
