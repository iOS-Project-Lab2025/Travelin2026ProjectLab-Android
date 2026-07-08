package com.softserveacademy.feature.booking.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.TravelPrimaryButton
import com.softserveacademy.core.presentation.design_system.components.util.buttons.PrimaryButtonVariant
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

@Composable
fun TravelHotelBookingBottomBar(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = TravelinDimens.ElevationSmall,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(TravelinDimens.PaddingMedium)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
        ) {
            TravelPrimaryButton(
                text = "Back",
                onClick = onBackClick,
                variant = PrimaryButtonVariant.Neutral,
                modifier = Modifier.weight(1f)
            )
            TravelPrimaryButton(
                text = "Next",
                onClick = onNextClick,
                variant = PrimaryButtonVariant.CallToAction,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview
@Composable
private fun TravelHotelBookingBottomBarPreview() {
    Travelin2026ProjectLabTheme {
        TravelHotelBookingBottomBar(
            onBackClick = {},
            onNextClick = {}
        )
    }
}