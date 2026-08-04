package com.softserveacademy.feature.booking.common.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.TravelPrimaryButton
import com.softserveacademy.core.presentation.design_system.components.util.buttons.PrimaryButtonVariant
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelPaymentSimulationSheet(
    onDismissRequest: () -> Unit,
    onSimulateSuccess: () -> Unit,
    onSimulateFailure: () -> Unit,
    simulationError: String? = null
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(TravelinDimens.PaddingMedium)
                .padding(bottom = TravelinDimens.Padding2ExtraLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
        ) {
            Text(
                text = "Failed to create payment intent",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = "There are no Stripe API keys",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            TravelPrimaryButton(
                text = "Simulate Success",
                onClick = onSimulateSuccess,
                variant = PrimaryButtonVariant.CallToAction
            )

            TravelPrimaryButton(
                text = "Simulate Failure",
                onClick = onSimulateFailure,
                variant = PrimaryButtonVariant.Neutral
            )

            if (simulationError != null) {
                Text(
                    text = simulationError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = TravelinDimens.SpaceSmall)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TravelPaymentSimulationSheetPreview() {
    Travelin2026ProjectLabTheme {
        TravelPaymentSimulationSheet(
            onDismissRequest = {},
            onSimulateSuccess = {},
            onSimulateFailure = {},
            simulationError = "Payment failed try again"
        )
    }
}
