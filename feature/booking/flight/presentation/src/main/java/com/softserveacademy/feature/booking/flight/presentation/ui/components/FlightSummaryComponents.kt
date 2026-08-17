package com.softserveacademy.feature.booking.flight.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.domain.model.FlightContactInfo
import com.softserveacademy.core.domain.model.FlightOffer
import com.softserveacademy.core.domain.model.FlightPassenger
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.flight.presentation.ui.mappers.toDisplayName

/**
 * Renders a read-only field that looks like an input box. Used for Dates, Guests, etc.
 */
@Composable
fun SummaryInfoCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Surface(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (icon != null) icon()
                Text(text = value, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

/**
 * Detailed Flight Card for Checkout.
 */
@Composable
fun FlightTicketSummaryCard(offer: FlightOffer, currencyCode: String) {
    FlightResultItem(offer = offer, isSelected = true, currencyCode = currencyCode, onClick = {})
}

/**
 * List of Travelers with Full Name in CamelCase and Passenger Type.
 */
@Composable
fun PassengerSummaryCard(passengers: List<FlightPassenger>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(TravelinDimens.PaddingMedium)) {
            Text(text = "Traveler Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(TravelinDimens.SpaceSmall))
            passengers.forEach { pax ->
                Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    // CAMEL CASE: "juan perez" -> "Juan Perez"
                    val formattedName = "${pax.firstName} ${pax.lastName}".lowercase()
                        .split(" ")
                        .filter { it.isNotEmpty() }
                        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }

                    Text(text = formattedName, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                    Text(text = pax.passengerType.toDisplayName(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

/**
 * Read-only Contact Card matching the design system style.
 */
@Composable
fun ContactSummaryCard(contact: FlightContactInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(TravelinDimens.PaddingMedium), verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)) {
            Text(text = "Contact Information", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            ReadOnlyField(label = "Email", value = contact.email)
            ReadOnlyField(label = "Phone", value = "${contact.countryCode} ${contact.phone}")
        }
    }
}

/**
 * Renders a read-only field that looks like an input box. Used for Dates, Guests, etc.
 */
@Composable
private fun ReadOnlyField(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}