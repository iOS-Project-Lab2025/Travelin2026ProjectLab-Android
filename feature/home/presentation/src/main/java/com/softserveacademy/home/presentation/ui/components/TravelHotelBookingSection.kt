package com.softserveacademy.home.presentation.ui.components

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
import androidx.compose.ui.Modifier
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.home.presentation.model.HotelInfoUi

/**
 * Section that displays accommodation details for a hotel booking.
 *
 * Shows the "Accommodation" header followed by a card with the hotel
 * name, room type, guest count, check-in/check-out dates, and the
 * confirmation code.
 *
 * @param hotel The hotel booking data to render.
 */
@Composable
fun TravelHotelBookingSection(hotel: HotelInfoUi) {
    Column {
        Text(
            text = "Accommodation",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = TravelinDimens.PaddingSmall)
        )

        Card(
            shape = RoundedCornerShape(TravelinDimens.SpaceSmall),
            elevation = CardDefaults.cardElevation(TravelinDimens.ElevationMedium),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(TravelinDimens.PaddingMedium)) {
                Text(
                    text = hotel.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))
                Row {
                    Text(
                        text = hotel.roomType,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${hotel.guests} guest${if (hotel.guests > 1) "s" else ""}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))
                Text(
                    text = "${hotel.checkIn} - ${hotel.checkOut}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))
                Text(
                    text = "Confirmation: ${hotel.confirmationCode}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
