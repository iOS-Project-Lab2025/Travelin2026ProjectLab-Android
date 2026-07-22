package com.softserveacademy.home.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.theme.Green50
import com.softserveacademy.core.presentation.design_system.theme.TicketIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.home.presentation.model.TicketUi

@Composable
fun TravelTicketCard(
    ticket: TicketUi,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val cardShape = remember(density) {
        BoardingPassShape(
            cornerRadius = 20.dp,
            notchRadius = 10.dp,
            notchHeightFraction = 0.68f,
            density = density
        )
    }

    Card(
        shape = cardShape,
        elevation = CardDefaults.cardElevation(TravelinDimens.ElevationMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(
                start = TravelinDimens.PaddingMedium,
                end = TravelinDimens.PaddingMedium,
                top = TravelinDimens.PaddingMedium,
                bottom = TravelinDimens.PaddingSmall
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ticket.passengerName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                StatusBadge(text = ticket.seatClass)
            }

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Seat",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = ticket.seatNumber ?: "—",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Gate",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = ticket.gate ?: "—",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                ticket.boardingGroup?.let { group ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Group",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = group,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

            PerforatedDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = TravelinDimens.PaddingNormal),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ticket",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = ticket.ticketNumber,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TravelTicketCardPreview() {
    Travelin2026ProjectLabTheme {
        TravelTicketCard(
            ticket = TicketUi(
                ticketNumber = "TK-123456",
                passengerName = "John Doe",
                seatNumber = "12A",
                gate = "B15",
                boardingGroup = "Group 2",
                seatClass = "Economy"
            ),
            modifier = Modifier.padding(TravelinDimens.PaddingMedium)
        )
    }
}
