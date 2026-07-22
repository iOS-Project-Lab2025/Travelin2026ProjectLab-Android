package com.softserveacademy.home.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.home.presentation.model.FlightBookingUi

/**
 * Section that displays a flight booking's flights and tickets.
 *
 * Shows the booking ID header, a [TravelFlightLegCard] for each flight,
 * then a list of [TravelTicketCard]s for passengers, the confirmation
 * code, and a [TravelStatusBadge] with the booking's current status.
 *
 * @param booking The flight booking data to render.
 */
@Composable
fun TravelFlightBookingSection(booking: FlightBookingUi) {
    Column {
        Text(
            text = "Booking ${booking.bookingId}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = TravelinDimens.PaddingSmall)
        )

        booking.flights.forEachIndexed { index, flight ->
            TravelFlightLegCard(flight = flight, label = "Flight ${index + 1}")
            if (index < booking.flights.size - 1) {
                Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))
            }
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

        Text(
            text = "Passengers",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = TravelinDimens.PaddingSmall)
        )

        booking.tickets.forEach { ticket ->
            TravelTicketCard(ticket = ticket)
            Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))
        }

        Text(
            text = "Confirmation: ${booking.confirmationCode}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        TravelStatusBadge(text = booking.status)
    }
}
