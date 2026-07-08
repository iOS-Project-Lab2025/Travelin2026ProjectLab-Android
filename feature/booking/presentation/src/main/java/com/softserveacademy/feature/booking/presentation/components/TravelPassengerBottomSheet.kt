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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.TravelPrimaryButton
import com.softserveacademy.core.presentation.design_system.components.util.buttons.PrimaryButtonVariant
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelPassengerBottomSheet(
    onDismissRequest: () -> Unit,
    onAccept: (adults: Int, kids: Int, pets: Int) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    var adults by remember { mutableIntStateOf(1) }
    var kids by remember { mutableIntStateOf(0) }
    var pets by remember { mutableIntStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        TravelPassengerBottomSheetContent(
            adults = adults,
            kids = kids,
            pets = pets,
            onAdultsChange = { adults = it },
            onKidsChange = { kids = it },
            onPetsChange = { pets = it },
            onAccept = {
                onAccept(adults, kids, pets)
                onDismissRequest()
            }
        )
    }
}

@Composable
fun TravelPassengerBottomSheetContent(
    adults: Int,
    kids: Int,
    pets: Int,
    onAdultsChange: (Int) -> Unit,
    onKidsChange: (Int) -> Unit,
    onPetsChange: (Int) -> Unit,
    onAccept: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(TravelinDimens.PaddingExtraLarge)
    ) {
        Text(
            text = stringResource(R.string.passengers_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.passengers_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

        TravelPassengerCounter(
            label = stringResource(R.string.adults_label),
            count = adults,
            onCountChange = onAdultsChange,
            minCount = 1
        )
        TravelPassengerCounter(
            label = stringResource(R.string.kids_label),
            subtitle = stringResource(R.string.kids_subtitle),
            count = kids,
            onCountChange = onKidsChange
        )
        TravelPassengerCounter(
            label = stringResource(R.string.pets_label),
            count = pets,
            onCountChange = onPetsChange
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

        TravelPrimaryButton(
            text = stringResource(R.string.accept_button_label),
            onClick = onAccept,
            variant = PrimaryButtonVariant.CallToAction
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))
    }
}

@Preview(showBackground = true)
@Composable
fun TravelPassengerBottomSheetPreview() {
    Travelin2026ProjectLabTheme {
        TravelPassengerBottomSheetContent(
            adults = 1,
            kids = 1,
            pets = 1,
            onAdultsChange = {},
            onKidsChange = {},
            onPetsChange = {},
            onAccept = {}
        )
    }
}
