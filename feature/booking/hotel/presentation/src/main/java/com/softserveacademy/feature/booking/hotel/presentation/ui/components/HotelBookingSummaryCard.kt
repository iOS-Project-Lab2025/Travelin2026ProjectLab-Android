package com.softserveacademy.feature.booking.hotel.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import java.text.SimpleDateFormat
import java.util.*

/**
 * A card component that displays a summary of the booking dates, duration, and guest information.
 *
 * @param checkIn The check-in date in milliseconds.
 * @param checkOut The check-out date in milliseconds.
 * @param guests A string representation of the guest information.
 * @param modifier The modifier to be applied to the card.
 */
@Composable
fun HotelBookingSummaryCard(
    checkIn: Long,
    checkOut: Long,
    guests: String,
    modifier: Modifier = Modifier
) {
    val nights = if (checkIn != 0L && checkOut != 0L) {
        ((checkOut - checkIn) / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(1)
    } else 1

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(TravelinDimens.PaddingMedium)) {
            BookingDateRow(label = "Check-in", date = checkIn)
            Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))
            BookingDateRow(label = "Check-out", date = checkOut)
            
            Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))
            
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(horizontal = TravelinDimens.PaddingMedium, vertical = TravelinDimens.PaddingSmall)
            ) {
                Text(
                    text = "$nights Nights, ${nights - 1} Days",
                    color = MaterialTheme.colorScheme.surface,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

            Text(
                text = "Guests",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))
            OutlinedTextField(
                value = guests,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )
        }
    }
}

/**
 * A row component for displaying a date and its label.
 *
 * @param label The label text.
 * @param date The value text.
 */
@Composable
fun BookingDateRow(
    label: String,
    date: Long
) {
    val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Light
        )
        Text(
            text = dateFormat.format(Date(date)),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview
@Composable
fun HotelBookingSummaryCardPreview() {
    Travelin2026ProjectLabTheme() {
        HotelBookingSummaryCard(
            checkIn = 1782115200000L, // Thursday, July 23, 2026
            checkOut = 1782374400000L, // Sunday, July 26, 2026
            guests = "2 adults, 1 child, pets"
        )
    }
}