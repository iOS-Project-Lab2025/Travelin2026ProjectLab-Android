package com.softserveacademy.feature.booking.flight.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.softserveacademy.core.domain.model.FlightOffer
import com.softserveacademy.core.presentation.design_system.theme.*
import com.softserveacademy.feature.booking.flight.presentation.R
import com.softserveacademy.feature.booking.flight.presentation.ui.mappers.toDisplayName
import com.softserveacademy.feature.booking.flight.presentation.util.rememberFlightTimeFormatter
import java.util.Date

@Composable
fun FlightResultItem(offer: FlightOffer, onClick: () -> Unit) {
    val timeFormat = rememberFlightTimeFormatter()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = TravelinDimens.PaddingNormal, horizontal = TravelinDimens.PaddingMedium),
        verticalAlignment = Alignment.Top
    ) {
        @OptIn(ExperimentalGlideComposeApi::class)
        GlideImage(
            model = offer.flight.airline.logoUrl,
            contentDescription = "Airline Logo",
            modifier = Modifier
                .size(TravelinDimens.IconSizeExtraLarge)
                .background(Color.White, shape = RoundedCornerShape(4.dp))
                .padding(4.dp),
            contentScale = androidx.compose.ui.layout.ContentScale.Fit,
        )

        Spacer(modifier = Modifier.width(TravelinDimens.SpaceMedium))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = offer.flight.airline.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${offer.flight.origin.city} → ${offer.flight.destination.city}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.Start) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = offer.flight.origin.code, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(Modifier.width(4.dp))
                        Icon(imageVector = PlaneTakeoffIcon, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurface)
                    }
                    Text(text = timeFormat.format(Date(offer.flight.departureTime)), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Box(modifier = Modifier.weight(1f).padding(horizontal = 8.dp), contentAlignment = Alignment.Center) {
                    DashedLine()
                    Text(
                        text = "Flight", // Podrías pasar la duración real aquí
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.background(MaterialTheme.colorScheme.background).padding(horizontal = 4.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = PlaneLandIcon, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurface)
                        Spacer(Modifier.width(4.dp))
                        Text(text = offer.flight.destination.code, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Text(text = timeFormat.format(Date(offer.flight.arrivalTime)), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Surface(
                    shape = RoundedCornerShape(TravelinDimens.Space2ExtraSmall),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
                    color = Color.Transparent
                ) {
                    Text(
                        text = offer.flight.cabinClass.toDisplayName(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$${String.format("%,d", offer.basePrice.toLong()).replace(',', '.')}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = " CLP /p.p",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 2.dp, start = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FlightEmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = PlaneIcon,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.outline
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.flight_empty_results),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun DashedLine() {
    val color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
    Canvas(Modifier.fillMaxWidth().height(1.dp)) {
        drawLine(
            color = color,
            start = androidx.compose.ui.geometry.Offset(0f, 0f),
            end = androidx.compose.ui.geometry.Offset(size.width, 0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f),
            strokeWidth = 1.dp.toPx()
        )
    }
}