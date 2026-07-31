package com.softserveacademy.feature.booking.flight.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.softserveacademy.core.domain.model.FlightOffer
import com.softserveacademy.core.presentation.design_system.theme.*
import com.softserveacademy.core.presentation.design_system.components.TravelLoadingScreen // Reutilizamos el loader del proyecto
import com.softserveacademy.feature.booking.common.presentation.ui.components.TravelBookingBottomBar
import com.softserveacademy.feature.booking.flight.presentation.R
import com.softserveacademy.feature.booking.flight.presentation.viewmodel.FlightResultsViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Stateful version of the Results screen.
 * Orchestrates navigation and connects the UI with the domain logic.
 */
@Composable
fun FlightResultsScreen(
    viewModel: FlightResultsViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    FlightResultsContent(
        offers = state.offers,
        origin = state.origin,
        destination = state.destination,
        passengerCount = state.totalPassengers,
        totalAvailable = state.totalAvailableCount,
        isLoading = state.isLoading,
        error = state.error,
        onNext = onNext,
        onBack = onBack,
        onLoadMore = { /* Event to load more flights */ }
    )
}

/**
 * Stateless content of the results.
 * Corrected to handle Loading and Error states professionally.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightResultsContent(
    offers: List<FlightOffer>,
    origin: String,
    destination: String,
    passengerCount: Int,
    totalAvailable: Int,
    isLoading: Boolean,
    error: String?,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onLoadMore: () -> Unit
) {
    if (isLoading) {
        TravelLoadingScreen() // Consistencia con el proyecto
    } else {
        Scaffold(
            topBar = {
                IconButton(onClick = onBack, modifier = Modifier.padding(start = 8.dp, top = 8.dp)) {
                    Icon(imageVector = ArrowLeftIcon, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
                }
            },
            bottomBar = {
                TravelBookingBottomBar(onBackClick = onBack, onNextClick = onNext)
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(modifier = Modifier.padding(padding).fillMaxSize()) {

                // Content Title
                Column(modifier = Modifier.padding(horizontal = TravelinDimens.PaddingMedium)) {
                    Text(
                        text = stringResource(R.string.flight_results_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$origin to $destination • $passengerCount Passengers",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

                if (error != null) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = error, color = MaterialTheme.colorScheme.error)
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        itemsIndexed(offers) { index, offer ->
                            FlightResultItem(offer = offer, onClick = { /* Logic */ })
                            if (index < offers.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = TravelinDimens.PaddingMedium),
                                    thickness = 0.5.dp,
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                                )
                            }
                        }

                        if (totalAvailable > offers.size) {
                            item {
                                val remaining = totalAvailable - offers.size
                                OutlinedButton(
                                    onClick = onLoadMore,
                                    modifier = Modifier.fillMaxWidth().padding(TravelinDimens.PaddingMedium),
                                    shape = RoundedCornerShape(TravelinDimens.SpaceSmall),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                                ) {
                                    Text(text = "Show +$remaining more available")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Clean list item following the "Upcoming Trip" aesthetics.
 * Uses official icons and provides high contrast for Dark Mode.
 */
@Composable
fun FlightResultItem(offer: FlightOffer, onClick: () -> Unit) {
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = TravelinDimens.PaddingNormal, horizontal = TravelinDimens.PaddingMedium),
        verticalAlignment = Alignment.Top
    ) {
        AsyncImage(
            model = offer.flight.airline.logoUrl,
            contentDescription = null,
            modifier = Modifier.size(TravelinDimens.IconSizeExtraLarge) // 40dp density
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

            // Professional Timeline: IATA Code + Icon
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.Start) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = offer.flight.origin.code, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(Modifier.width(4.dp))
                        Icon(imageVector = PlaneTakeoffIcon, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurface)
                    }
                    Text(text = timeFormat.format(Date(offer.flight.departureTime)), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                // Dotted Connection
                Box(modifier = Modifier.weight(1f).padding(horizontal = 8.dp), contentAlignment = Alignment.Center) {
                    DashedLine()
                    Text(
                        text = "1h 30m",
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

            // Footer: Cabin Class and Formatted Price
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Surface(
                    shape = RoundedCornerShape(TravelinDimens.Space2ExtraSmall),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
                    color = Color.Transparent
                ) {
                    Text(
                        text = "Economic class",
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

// --- Previews: Moving Data out of Production Code ---

@Preview(showBackground = true, name = "Results - Light Mode")
@Composable
fun FlightResultsPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        FlightResultsContent(
            offers = getMockPreviewList(),
            origin = "SCL",
            destination = "LIM",
            passengerCount = 2,
            totalAvailable = 20, // Simulamos que hay 20 en total
            isLoading = false,
            error = null,
            onNext = {},
            onBack = {},
            onLoadMore = {}
        )
    }
}

@Preview(showBackground = true, name = "Results - Dark Mode")
@Composable
fun FlightResultsDarkPreview() {
    Travelin2026ProjectLabTheme(darkTheme = true) {
        FlightResultsContent(
            offers = getMockPreviewList(),
            origin = "SCL",
            destination = "LIM",
            passengerCount = 2,
            totalAvailable = 20, // El botón dirá +17 (20 total - 3 en lista)
            isLoading = false,
            error = null,
            onNext = {},
            onBack = {},
            onLoadMore = {}
        )
    }
}

/**
 * Data mock específica para Previews.
 * Muestra el formato de precio de la imagen (100.000.000).
 */
private fun getMockPreviewList() = listOf(
    com.softserveacademy.core.domain.model.FlightOffer(
        id = "1",
        flight = com.softserveacademy.core.domain.model.Flight(
            id = "F1",
            airline = com.softserveacademy.core.domain.model.Airline("LA", "Latam Airlines", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/LATAM_Logo.svg/512px-LATAM_Logo.svg.png"),
            flightNumber = "LA123",
            origin = com.softserveacademy.core.domain.model.Airport("SCL", "Santiago", "SCL", "Chile"),
            destination = com.softserveacademy.core.domain.model.Airport("LIM", "Lima", "LIM", "Peru"),
            departureTime = System.currentTimeMillis(),
            arrivalTime = System.currentTimeMillis() + 5400000,
            duration = kotlin.time.Duration.parse("1h 30m"),
            cabinClass = com.softserveacademy.core.domain.model.CabinClass.FIRST
        ),
        basePrice = 100000000.0
    ),
    com.softserveacademy.core.domain.model.FlightOffer(
        id = "2",
        flight = com.softserveacademy.core.domain.model.Flight(
            id = "F2",
            airline = com.softserveacademy.core.domain.model.Airline("LA", "Latam Airlines", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/LATAM_Logo.svg/512px-LATAM_Logo.svg.png"),
            flightNumber = "LA456",
            origin = com.softserveacademy.core.domain.model.Airport("SCL", "Santiago", "SCL", "Chile"),
            destination = com.softserveacademy.core.domain.model.Airport("LIM", "Lima", "LIM", "Peru"),
            departureTime = System.currentTimeMillis() + 10000000,
            arrivalTime = System.currentTimeMillis() + 15400000,
            duration = kotlin.time.Duration.parse("1h 30m"),
            cabinClass = com.softserveacademy.core.domain.model.CabinClass.ECONOMY
        ),
        basePrice = 500000.0
    ),
    com.softserveacademy.core.domain.model.FlightOffer(
        id = "3",
        flight = com.softserveacademy.core.domain.model.Flight(
            id = "F3",
            airline = com.softserveacademy.core.domain.model.Airline("LA", "Latam Airlines", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/LATAM_Logo.svg/512px-LATAM_Logo.svg.png"),
            flightNumber = "LA789",
            origin = com.softserveacademy.core.domain.model.Airport("SCL", "Santiago", "SCL", "Chile"),
            destination = com.softserveacademy.core.domain.model.Airport("LIM", "Lima", "LIM", "Peru"),
            departureTime = System.currentTimeMillis() + 20000000,
            arrivalTime = System.currentTimeMillis() + 25400000,
            duration = kotlin.time.Duration.parse("1h 30m"),
            cabinClass = com.softserveacademy.core.domain.model.CabinClass.BUSINESS
        ),
        basePrice = 1000000.0
    )
)