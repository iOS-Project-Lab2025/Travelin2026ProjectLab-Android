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
import com.softserveacademy.home.presentation.model.FlightBookingUi
import com.softserveacademy.home.presentation.model.FlightUi
import com.softserveacademy.home.presentation.model.TicketUi
import com.softserveacademy.home.presentation.model.TripDetailUi
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@Composable
fun TravelTripSummaryCard(
    tripDetail: TripDetailUi,
    modifier: Modifier = Modifier
) {
    val firstFlight = tripDetail.flightBookings.firstOrNull()?.flights?.firstOrNull()
    val flightCount = tripDetail.flightBookings.sumOf { it.flights.size }
    val passengerCount = tripDetail.flightBookings.sumOf { it.tickets.size }

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
                StatusBadge(text = "Upcoming")
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${tripDetail.startDate} - ${tripDetail.endDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

            if (firstFlight != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoutePoint(
                        code = firstFlight.origin.code,
                        time = firstFlight.departureTime,
                        icon = PlaneTakeoffIcon
                    )

                    DurationConnector(
                        duration = firstFlight.duration.let {
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
                        code = firstFlight.destination.code,
                        time = firstFlight.arrivalTime,
                        icon = PlaneLandIcon,
                        alignEnd = true
                    )
                }

                Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

                Text(
                    text = "${firstFlight.airline} · ${firstFlight.cabinClass}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))
            }

            Text(
                text = "$flightCount flight${if (flightCount != 1) "s" else ""} · $passengerCount passenger${if (passengerCount != 1) "s" else ""}",
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
                    text = "Destination",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = tripDetail.destination,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TravelTripSummaryCardPreview() {
    val mockTrip = TripDetailUi(
        tripId = "trip_001",
        destination = "Bali",
        startDate = "Sat, Nov 23",
        endDate = "Thu, Nov 28",
        flightBookings = listOf(
            FlightBookingUi(
                bookingId = "GA-880",
                flights = listOf(
                    FlightUi(
                        flightNumber = "GA880",
                        airline = "Garuda Indonesia",
                        airlineLogo = "",
                        origin = Airport("CGK", "Soekarno-Hatta", "Jakarta", "Indonesia"),
                        destination = Airport("DPS", "Ngurah Rai", "Denpasar", "Indonesia"),
                        departureTime = "09:00",
                        arrivalTime = "10:30",
                        duration = 1.hours + 30.minutes,
                        cabinClass = "Economy"
                    )
                ),
                tickets = listOf(
                    TicketUi(
                        ticketNumber = "TK-123456",
                        passengerName = "John Doe",
                        seatNumber = "12A",
                        gate = "B15",
                        boardingGroup = "Group 2",
                        seatClass = "Economy"
                    ),
                    TicketUi(
                        ticketNumber = "TK-123457",
                        passengerName = "Jane Doe",
                        seatNumber = "12B",
                        gate = "B15",
                        boardingGroup = "Group 2",
                        seatClass = "Economy"
                    )
                ),
                confirmationCode = "ABC123",
                status = "Confirmed"
            )
        ),
        hotelInfo = null,
        tourInfo = null
    )

    Travelin2026ProjectLabTheme {
        TravelTripSummaryCard(
            tripDetail = mockTrip,
            modifier = Modifier.padding(TravelinDimens.PaddingMedium)
        )
    }
}
