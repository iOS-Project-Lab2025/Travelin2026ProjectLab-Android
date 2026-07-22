package com.softserveacademy.home.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.presentation.design_system.theme.PlaneLandIcon
import com.softserveacademy.core.presentation.design_system.theme.PlaneTakeoffIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.home.presentation.model.FlightUi
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@Composable
fun TravelFlightLegCard(
    flight: FlightUi,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(TravelinDimens.SpaceSmall),
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
                StatusBadge(text = label)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = flight.cabinClass,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoutePoint(
                    code = flight.origin.code,
                    time = flight.departureTime,
                    icon = PlaneTakeoffIcon
                )

                DurationConnector(
                    duration = flight.duration.let {
                        val h = it.inWholeHours
                        val m = it.inWholeMinutes % 60
                        buildString {
                            if (h > 0) append("${h}h ")
                            append("${m}m")
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = TravelinDimens.PaddingSmall)
                )

                RoutePoint(
                    code = flight.destination.code,
                    time = flight.arrivalTime,
                    icon = PlaneLandIcon,
                    alignEnd = true
                )
            }

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

            Text(
                text = "${flight.airline} · ${flight.flightNumber}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

            PerforatedDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = TravelinDimens.PaddingNormal),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = flight.origin.city,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = flight.destination.city,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TravelFlightLegCardPreview() {
    Travelin2026ProjectLabTheme {
        TravelFlightLegCard(
            flight = FlightUi(
                flightNumber = "GA880",
                airline = "Garuda Indonesia",
                airlineLogo = "",
                origin = Airport("CGK", "Soekarno-Hatta", "Jakarta", "Indonesia"),
                destination = Airport("DPS", "Ngurah Rai", "Denpasar", "Indonesia"),
                departureTime = "09:00",
                arrivalTime = "10:30",
                duration = 1.hours + 30.minutes,
                cabinClass = "Economy"
            ),
            label = "Flight 1",
            modifier = Modifier.padding(TravelinDimens.PaddingMedium)
        )
    }
}
