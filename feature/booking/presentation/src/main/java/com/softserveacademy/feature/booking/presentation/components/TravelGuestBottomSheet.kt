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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.InlineErrorBanner
import com.softserveacademy.core.presentation.design_system.components.TravelLabelCounter
import com.softserveacademy.core.presentation.design_system.components.TravelLabelSwitch
import com.softserveacademy.core.presentation.design_system.components.TravelPrimaryButton
import com.softserveacademy.core.presentation.design_system.components.util.buttons.PrimaryButtonVariant
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.presentation.R

/**
 * A bottom sheet for selecting the number of guests (adults, kids, pets).
 *
 * @param modifier The modifier to be applied to the bottom sheet.
 * @param adults The current number of adults.
 * @param kids The current number of children.
 * @param hasPets Whether pets are included.
 * @param onAdultsChange The callback to be invoked when the number of adults changes.
 * @param onKidsChange The callback to be invoked when the number of children changes.
 * @param onHasPetsChange The callback to be invoked when the pets toggle changes.
 * @param onAccept The callback to be invoked when the selection is accepted.
 * @param onDismissRequest The callback to be invoked when the bottom sheet is dismissed.
 * @param isErrorVisible Whether the error message is visible.
 * @param errorMessage The error message to be displayed.
 * @param sheetState The state of the bottom sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelGuestBottomSheet(
    modifier: Modifier = Modifier,
    adults: Int,
    kids: Int,
    hasPets: Boolean,
    onAdultsChange: (Int) -> Unit,
    onKidsChange: (Int) -> Unit,
    onHasPetsChange: (Boolean) -> Unit,
    onAccept: () -> Unit,
    onDismissRequest: () -> Unit,
    isErrorVisible: Boolean = false,
    errorMessage: String? = null,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
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
            onAdultsChange = onAdultsChange,
            onKidsChange = onKidsChange,
            onHasPetsChange = onHasPetsChange,
            onAccept = onAccept,
            isErrorVisible = isErrorVisible,
            errorMessage = errorMessage
        )
    }
}

/**
 * Internal composable that represents the content of the guest selection bottom sheet.
 *
 * @param modifier The modifier to be applied to the content.
 * @param adults The current number of adults.
 * @param kids The current number of children.
 * @param hasPets Whether pets are included.
 * @param onAdultsChange The callback to be invoked when the number of adults changes.
 * @param onKidsChange The callback to be invoked when the number of children changes.
 * @param onHasPetsChange The callback to be invoked when the pets toggle changes.
 * @param onAccept The callback to be invoked when the selection is accepted.
 * @param isErrorVisible Whether the error message is visible.
 * @param errorMessage The error message to be displayed.
 */
@Composable
fun TravelGuestBottomSheetContent(
    modifier: Modifier = Modifier,
    adults: Int,
    kids: Int,
    hasPets: Boolean,
    onAdultsChange: (Int) -> Unit,
    onKidsChange: (Int) -> Unit,
    onHasPetsChange: (Boolean) -> Unit,
    onAccept: () -> Unit,
    isErrorVisible: Boolean = false,
    errorMessage: String? = null,
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

        InlineErrorBanner(
            message = errorMessage ?: "",
            isVisible = isErrorVisible,
            modifier = Modifier.padding(bottom = TravelinDimens.PaddingMedium)
        )

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
