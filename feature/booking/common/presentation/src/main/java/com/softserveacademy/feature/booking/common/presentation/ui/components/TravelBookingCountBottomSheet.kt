package com.softserveacademy.feature.booking.common.presentation.ui.components

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
import com.softserveacademy.feature.booking.common.presentation.R
import com.softserveacademy.feature.booking.common.presentation.ui.components.util.TravelBookingCountItem

/**
 * A generic bottom sheet for selecting various counts and toggles (e.g., guests, rooms, etc.).
 *
 * @param modifier The modifier to be applied to the bottom sheet.
 * @param items The list of [TravelBookingCountItem] to be displayed.
 * @param onAccept The callback to be invoked when the selection is accepted.
 * @param onDismissRequest The callback to be invoked when the bottom sheet is dismissed.
 * @param isErrorVisible Whether the error message is visible.
 * @param errorMessage The error message to be displayed.
 * @param sheetState The state of the bottom sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelBookingCountBottomSheet(
    modifier: Modifier = Modifier,
    items: List<TravelBookingCountItem>,
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
        TravelBookingCountBottomSheetContent(
            items = items,
            onAccept = onAccept,
            isErrorVisible = isErrorVisible,
            errorMessage = errorMessage
        )
    }
}

/**
 * Internal composable that represents the content of the booking count selection bottom sheet.
 *
 * @param modifier The modifier to be applied to the content.
 * @param items The list of [TravelBookingCountItem] to be displayed.
 * @param onAccept The callback to be invoked when the selection is accepted.
 * @param isErrorVisible Whether the error message is visible.
 * @param errorMessage The error message to be displayed.
 */
@Composable
fun TravelBookingCountBottomSheetContent(
    modifier: Modifier = Modifier,
    items: List<TravelBookingCountItem>,
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

        items.forEach { item ->
            when (item) {
                is TravelBookingCountItem.Counter -> {
                    TravelLabelCounter(
                        label = item.label,
                        subtitle = item.subtitle,
                        count = item.count,
                        onCountChange = item.onCountChange,
                        minCount = item.minCount,
                        maxCount = item.maxCount
                    )
                }
                is TravelBookingCountItem.Switch -> {
                    TravelLabelSwitch(
                        label = item.label,
                        subtitle = item.subtitle,
                        checked = item.checked,
                        onCheckedChange = item.onCheckedChange
                    )
                }
            }
        }

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
fun TravelBookingCountBottomSheetPreview() {
    Travelin2026ProjectLabTheme {
        TravelBookingCountBottomSheetContent(
            items = listOf(
                TravelBookingCountItem.Counter(
                    label = "Adults",
                    count = 1,
                    onCountChange = {},
                    minCount = 1
                ),
                TravelBookingCountItem.Counter(
                    label = "Kids",
                    subtitle = "0 - 17 years",
                    count = 0,
                    onCountChange = {}
                ),
                TravelBookingCountItem.Switch(
                    label = "Do you travel with pets?",
                    checked = false,
                    onCheckedChange = {}
                )
            ),
            onAccept = {}
        )
    }
}
