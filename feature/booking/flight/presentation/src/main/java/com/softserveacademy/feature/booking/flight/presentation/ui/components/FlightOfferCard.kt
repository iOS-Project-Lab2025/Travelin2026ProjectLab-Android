package com.softserveacademy.feature.booking.flight.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.softserveacademy.core.domain.model.FlightOffer
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import java.text.SimpleDateFormat
import java.util.*

/**
 * A reusable card to display flight offer details in search results.
 *
 * @param offer The flight offer data to display.
 * @param onClick Callback when the card is selected.
 */
@Composable
fun FlightOfferCard(
    offer: FlightOffer,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(TravelinDimens.PaddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = offer.flight.airline.logoUrl,
                contentDescription = offer.flight.airline.name,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(TravelinDimens.SpaceMedium))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = offer.flight.airline.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = timeFormat.format(Date(offer.flight.departureTime)),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = " - ", modifier = Modifier.padding(horizontal = 4.dp))
                    Text(
                        text = timeFormat.format(Date(offer.flight.arrivalTime)),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "${offer.flight.origin.code} to ${offer.flight.destination.code}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${offer.basePrice}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "/per person",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}