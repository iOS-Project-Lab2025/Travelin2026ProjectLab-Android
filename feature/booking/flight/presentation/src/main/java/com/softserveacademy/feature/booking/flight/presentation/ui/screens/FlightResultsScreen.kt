package com.softserveacademy.feature.booking.flight.presentation.ui.screens

import androidx.compose.foundation.BorderStroke

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.domain.model.FlightOffer
import com.softserveacademy.core.presentation.design_system.components.TravelLoadingScreen
import com.softserveacademy.core.presentation.design_system.components.TravelPrimaryButton
import com.softserveacademy.core.presentation.design_system.theme.*
import com.softserveacademy.feature.booking.common.presentation.ui.components.TravelBookingBottomBar
import com.softserveacademy.feature.booking.flight.presentation.R
import com.softserveacademy.feature.booking.flight.presentation.events.FlightResultsEvent
import com.softserveacademy.feature.booking.flight.presentation.ui.components.FlightEmptyState
import com.softserveacademy.feature.booking.flight.presentation.ui.components.FlightResultItem

import com.softserveacademy.feature.booking.flight.presentation.viewmodel.FlightResultsViewModel


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

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect {
            onNext() //triggers navigation to US3
        }
    }

    LaunchedEffect(Unit) {
        // Escucha para retroceder (salir de resultados)
        viewModel.backNavigationEvent.collect { onBack() }
    }

    FlightResultsContent(
        visibleOffers = state.visibleOffers,
        origin = state.origin,
        destination = state.destination,
        passengerCount = state.totalPassengers,
        totalAvailable = state.totalAvailableCount,
        isLoading = state.isLoading,
        error = state.error,
        currentSegmentIndex = state.currentSegmentIndex,
        totalSegments = state.totalSegments,
        selectedOfferId = state.selectedOfferId,
        isNextEnabled = state.selectedOfferId != null,
        currencyCode = state.currencyCode,
        exchangeRate = state.exchangeRate,
        onNext = { viewModel.onEvent(FlightResultsEvent.OnNextClick) },
        onBack = { viewModel.onEvent(FlightResultsEvent.OnBackClick) },
        onLoadMore = { viewModel.onEvent(FlightResultsEvent.OnLoadMore) },
        onRetry = { viewModel.onEvent(FlightResultsEvent.OnRetryClick) },
        onFlightSelected = { id ->
            viewModel.onEvent(FlightResultsEvent.OnFlightSelected(id))
        }
    )
}

/**
 * Stateless content of the results.
 * Corrected to handle Loading and Error states professionally.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightResultsContent(
    visibleOffers: List<FlightOffer>,
    origin: String,
    destination: String,
    passengerCount: Int,
    totalAvailable: Int,
    isLoading: Boolean,
    error: Int?,
    isNextEnabled: Boolean,
    currentSegmentIndex: Int,
    totalSegments: Int,
    selectedOfferId: String?,
    currencyCode: String,
    exchangeRate: Double,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    onFlightSelected: (String) -> Unit
) {
    if (isLoading) {
        TravelLoadingScreen()
    } else {
        Scaffold(
            topBar = {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.padding(
                        start = TravelinDimens.PaddingSmall,
                        top = TravelinDimens.PaddingSmall
                    )
                ) {
                    Icon(
                        imageVector = ArrowLeftIcon,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            bottomBar = {
                //restores bottom buttons if there is no network error
                if (error == null) {
                    TravelBookingBottomBar(
                        onBackClick = onBack,
                        onNextClick = onNext,
                        nextButtonEnabled = isNextEnabled
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {

                    // Header Info (Always visible if no network error)
                    if (error == null) {
                        Column(modifier = Modifier.padding(horizontal = TravelinDimens.PaddingMedium)) {
                            if (totalSegments > 1) {
                                Text(
                                    text = stringResource(
                                        R.string.flight_segment_progress_format,
                                        currentSegmentIndex + 1,
                                        totalSegments
                                    ),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = TravelinDimens.PaddingSmall)
                                )
                            }
                            Text(
                                text = stringResource(
                                    R.string.flight_results_subtitle_format,
                                    origin,
                                    destination,
                                    passengerCount
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))
                    }

                    // INTERN ORCHESTATION OF STATES
                    when {
                        error != null -> {
                            // Pantalla de Error
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = WarningIcon,
                                    contentDescription = null,
                                    modifier = Modifier.size(TravelinDimens.IconSizeExtraLarge + 24.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Spacer(Modifier.height(TravelinDimens.SpaceMedium))
                                Text(
                                    text = stringResource(error),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = TravelinDimens.SpaceExtraLarge)
                                )
                                Spacer(Modifier.height(TravelinDimens.SpaceLarge))
                                TravelPrimaryButton(
                                    text = stringResource(R.string.flight_retry_button),
                                    onClick = onRetry,
                                    modifier = Modifier.width(150.dp)
                                )
                            }
                        }

                        visibleOffers.isEmpty() -> {
                            FlightEmptyState()
                        }

                        else -> {
                            // Lista de Vuelos (Ahora sí dentro de la Column principal)
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                itemsIndexed(visibleOffers) { _, offer ->
                                    FlightResultItem(
                                        offer = offer,
                                        isSelected = offer.id == selectedOfferId,
                                        currencyCode = currencyCode,
                                        exchangeRate = exchangeRate,
                                        onClick = { onFlightSelected(offer.id) }
                                    )
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = TravelinDimens.PaddingMedium),
                                        thickness = 1.dp,
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                    )
                                }

                                if (totalAvailable > visibleOffers.size) {
                                    item {
                                        val remaining = totalAvailable - visibleOffers.size
                                        OutlinedButton(
                                            onClick = onLoadMore,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("load_more_button")
                                                .padding(TravelinDimens.PaddingMedium),
                                            shape = RoundedCornerShape(TravelinDimens.SpaceSmall),
                                            border = BorderStroke(
                                                1.dp,
                                                MaterialTheme.colorScheme.outline
                                            )
                                        ) {
                                            Text(
                                                text = stringResource(
                                                    R.string.flight_result_see_more_button,
                                                    remaining
                                                ),
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


// --- Previews (Light & Dark Modes) ---

@Preview(showBackground = true, name = "Results - Light Mode")
@Composable
fun FlightResultsPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        FlightResultsContent(
            visibleOffers = getMockPreviewList(),
            origin = "SCL",
            destination = "LIM",
            passengerCount = 2,
            totalAvailable = 20,
            isLoading = false,
            error = null,
            currentSegmentIndex = 0,
            totalSegments = 3,
            selectedOfferId = null,
            currencyCode = "USD",
            exchangeRate = 1.0,
            onNext = {},
            onBack = {},
            isNextEnabled = false,
            onLoadMore = {},
            onRetry = {},
            onFlightSelected = {}
        )
    }
}

@Preview(showBackground = true, name = "Results - Dark Mode")
@Composable
fun FlightResultsDarkPreview() {
    Travelin2026ProjectLabTheme(darkTheme = true) {
        FlightResultsContent(
            visibleOffers = getMockPreviewList(),
            origin = "SCL",
            destination = "LIM",
            passengerCount = 2,
            totalAvailable = 20,
            isLoading = false,
            error = null,
            currentSegmentIndex = 0,
            totalSegments = 3,
            selectedOfferId = null,
            currencyCode = "USD",
            exchangeRate = 1.0,
            onNext = {},
            onBack = {},
            isNextEnabled = false,
            onLoadMore = {},
            onRetry = {},
            onFlightSelected = {}
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