package com.softserveacademy.feature.booking.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

@Composable
fun TravelPassengerCounter(
    label: String,
    count: Int,
    onCountChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    minCount: Int = 0,
    maxCount: Int = 10
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = TravelinDimens.PaddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        TravelCounter(
            count = count,
            onCountChange = onCountChange,
            minCount = minCount,
            maxCount = maxCount
        )
    }
}

@Preview
@Composable
fun TravelPassengerCounterPreview() {
    Travelin2026ProjectLabTheme() {
        TravelPassengerCounter(
            label = "Adults",
            count = 1,
            onCountChange = {},
            modifier = Modifier
        )
    }
}