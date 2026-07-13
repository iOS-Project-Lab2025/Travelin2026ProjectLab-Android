package com.softserveacademy.home.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.softserveacademy.core.presentation.design_system.components.TravelPrimaryButton
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * A booking bar component fixed at the bottom of the screen.
 * Displays the price per person and a "Book Now" button.
 *
 * @param price The price to display.
 * @param onBookClick The action to perform when the "Book Now" button is clicked.
 * @param modifier The modifier to be applied to the bar.
 */
@Composable
fun TravelBookingBar(
    price: Int,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = TravelinDimens.PaddingExtraLarge,
                vertical = TravelinDimens.PaddingSmall
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall)
        ){
            Text(
                text = "Starting from",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryFixed
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    ) {
                        append("\$$price")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    ) {
                        append("/Person")
                    }
                },
                style = MaterialTheme.typography.displaySmall
            )
        }

        // Book Now Button
        TravelPrimaryButton(
            text = "Book Now",
            onClick = onBookClick,
            modifier = Modifier.fillMaxWidth(0.7f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TravelBookingBarPreview() {
    Travelin2026ProjectLabTheme {
        TravelBookingBar(
            price = 600,
            onBookClick = {}
        )
    }
}
