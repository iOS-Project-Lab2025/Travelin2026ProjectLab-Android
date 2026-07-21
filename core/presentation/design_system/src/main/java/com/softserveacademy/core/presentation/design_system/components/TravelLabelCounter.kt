package com.softserveacademy.core.presentation.design_system.components

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

/**
 * A component that displays a label and an optional subtitle alongside a [TravelCounter].
 *
 * @param label The main label text.
 * @param count The current count value.
 * @param onCountChange The callback to be invoked when the count value changes.
 * @param modifier The modifier to be applied to the component.
 * @param subtitle An optional subtitle text displayed below the label.
 * @param minCount The minimum allowed count value.
 * @param maxCount The maximum allowed count value.
 */
@Composable
fun TravelLabelCounter(
    label: String,
    count: Int,
    onCountChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    minCount: Int = 0,
    maxCount: Int = 50
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
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
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

@Preview(widthDp = 250)
@Composable
fun TravelLabelCounterPreview() {
    Travelin2026ProjectLabTheme() {
        TravelLabelCounter(
            label = "Kids",
            subtitle = "0 - 17 years",
            count = 1,
            onCountChange = {},
            modifier = Modifier
        )
    }
}