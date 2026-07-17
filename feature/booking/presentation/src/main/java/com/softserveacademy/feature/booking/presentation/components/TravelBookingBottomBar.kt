package com.softserveacademy.feature.booking.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.TravelPrimaryButton
import com.softserveacademy.core.presentation.design_system.components.util.buttons.PrimaryButtonVariant
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.presentation.R

/**
 * A bottom bar component for the booking search screen, containing "Back" and "Next" buttons.
 *
 * @param onBackClick The callback to be invoked when the back button is clicked.
 * @param onNextClick The callback to be invoked when the next button is clicked.
 * @param modifier The modifier to be applied to the bottom bar.
 */
@Composable
fun TravelBookingBottomBar(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
    nextButtonEnabled: Boolean = true
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
                text = stringResource(R.string.back_button_label),
                onClick = onBackClick,
                variant = PrimaryButtonVariant.SecondaryAction,
                modifier = Modifier.weight(1f)
            )
            TravelPrimaryButton(
                text = stringResource(R.string.next_button_label),
                onClick = onNextClick,
                variant = PrimaryButtonVariant.CallToAction,
                enabled = nextButtonEnabled,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview
@Composable
private fun TravelBookingBottomBarPreview() {
    Travelin2026ProjectLabTheme {
        TravelBookingBottomBar(
            onBackClick = {},
            onNextClick = {}
        )
    }
}
