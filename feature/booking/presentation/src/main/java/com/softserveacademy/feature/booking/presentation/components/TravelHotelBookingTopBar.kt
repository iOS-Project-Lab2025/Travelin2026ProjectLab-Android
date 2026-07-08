package com.softserveacademy.feature.booking.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
import com.softserveacademy.core.presentation.design_system.theme.AngleLeftIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

@Composable
fun TravelHotelBookingTopBar(
    onBackClick: () -> Unit,
    title: String = "Title"
) {
    Surface (
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = TravelinDimens.ElevationSmall,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row (
            modifier = Modifier
                .padding(TravelinDimens.PaddingExtraSmall)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TravelIconButton(
                icon = AngleLeftIcon,
                onClick = onBackClick
            )
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }
}

@Preview
@Composable
private fun TravelHotelBookingTopBarPreview() {
    Travelin2026ProjectLabTheme {
        TravelHotelBookingTopBar(
            onBackClick = {},
            "Choose your booking"
        )
    }
}