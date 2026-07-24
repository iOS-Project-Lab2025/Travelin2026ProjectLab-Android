package com.softserveacademy.feature.booking.common.presentation.ui.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softserveacademy.core.presentation.design_system.components.TravelPrimaryButton
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.common.presentation.R

/**
 * A common bottom bar component for booking confirmation screens.
 * Displays a total price section on the left and a primary action button on the right.
 *
 * @param totalPrice The total price value to display.
 * @param buttonText The text to display on the primary action button.
 * @param onButtonClick Callback to be invoked when the button is clicked.
 * @param modifier The modifier to be applied to the bottom bar.
 */
@Composable
fun TravelBookingConfirmBottomBar(
    totalPrice: Int,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(TravelinDimens.PaddingMedium)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val totalInfo = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                ) {
                    append(stringResource(R.string.booking_confirm_total_label))
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                    )
                ) {
                    append("$$totalPrice")
                }
            }

            Text(
                text = totalInfo,
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.weight(1f)
            )
            TravelPrimaryButton(
                text = buttonText,
                onClick = onButtonClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
