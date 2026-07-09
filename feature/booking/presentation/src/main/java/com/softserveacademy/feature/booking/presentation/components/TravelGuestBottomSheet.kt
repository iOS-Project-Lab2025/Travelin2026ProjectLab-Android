package com.softserveacademy.feature.booking.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.TravelPrimaryButton
import com.softserveacademy.core.presentation.design_system.components.util.buttons.PrimaryButtonVariant
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelGuestBottomSheet(
    onDismissRequest: () -> Unit,
    onAccept: (adults: Int, kids: Int, hasPets: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    var adults by remember { mutableIntStateOf(1) }
    var kids by remember { mutableIntStateOf(0) }
    var hasPets by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
    ) {
        TravelGuestBottomSheetContent(
            adults = adults,
            kids = kids,
            hasPets = hasPets,
            onAdultsChange = { adults = it },
            onKidsChange = { kids = it },
            onHasPetsChange = { hasPets = it },
            onAccept = {
                onAccept(adults, kids, hasPets)
                onDismissRequest()
            }
        )
    }
}

@Composable
fun TravelGuestBottomSheetContent(
    adults: Int,
    kids: Int,
    hasPets: Boolean,
    onAdultsChange: (Int) -> Unit,
    onKidsChange: (Int) -> Unit,
    onHasPetsChange: (Boolean) -> Unit,
    onAccept: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(TravelinDimens.PaddingExtraLarge)
    ) {
        Text(
            text = stringResource(R.string.guest_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = stringResource(R.string.guest_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

        TravelLabelCounter(
            label = stringResource(R.string.adults_label),
            count = adults,
            onCountChange = onAdultsChange,
            minCount = 1
        )
        TravelLabelCounter(
            label = stringResource(R.string.kids_label),
            subtitle = stringResource(R.string.kids_subtitle),
            count = kids,
            onCountChange = onKidsChange
        )
        TravelLabelSwitch(
            label = stringResource(R.string.pets_label),
            checked = hasPets,
            onCheckedChange = onHasPetsChange
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

        TravelPrimaryButton(
            text = stringResource(R.string.accept_button_label),
            onClick = onAccept,
            variant = PrimaryButtonVariant.CallToAction
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TravelGuestBottomSheetPreview() {
    Travelin2026ProjectLabTheme {
        TravelGuestBottomSheetContent(
            adults = 1,
            kids = 0,
            hasPets = false,
            onAdultsChange = {},
            onKidsChange = {},
            onHasPetsChange = {},
            onAccept = {}
        )
    }
}
