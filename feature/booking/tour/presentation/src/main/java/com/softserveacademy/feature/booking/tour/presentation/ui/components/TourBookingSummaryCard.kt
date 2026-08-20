package com.softserveacademy.feature.booking.tour.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.tour.presentation.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * A card component that displays a summary of the booking dates and participant information for a tour.
 *
 * @param startDate The start date of the tour in milliseconds.
 * @param endDate The end date of the tour in milliseconds (optional).
 * @param participants A string representation of the participant information.
 * @param modifier The modifier to be applied to the card.
 */
@Composable
fun TourBookingSummaryCard(
    startDate: Long,
    endDate: Long,
    participants: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(TravelinDimens.PaddingMedium)) {
            if (startDate == endDate) {
                BookingDateRow(
                    label = stringResource(R.string.date_label),
                    date = startDate
                )
            } else {
                BookingDateRow(
                    label = stringResource(R.string.start_date_label),
                    date = startDate
                )
                Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))
                BookingDateRow(
                    label = stringResource(R.string.end_date_label),
                    date = endDate
                )
            }
            
            Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

            Text(
                text = stringResource(R.string.participants_label),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))
            OutlinedTextField(
                value = participants,
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
private fun BookingDateRow(
    label: String,
    date: Long
) {
    val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
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

@Preview(showBackground = true)
@Composable
fun TourBookingSummaryCardSingleDatePreview() {
    Travelin2026ProjectLabTheme {
        TourBookingSummaryCard(
            startDate = 1782115200000L, // Thursday, July 23, 2026
            endDate = 1782115200000L, // Thursday, July 23, 2026
            participants = "2 adults, 1 child"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TourBookingSummaryCardDateRangePreview() {
    Travelin2026ProjectLabTheme {
        TourBookingSummaryCard(
            startDate = 1782115200000L, // Thursday, July 23, 2026
            endDate = 1782374400000L, // Sunday, July 26, 2026
            participants = "4 adults"
        )
    }
}
