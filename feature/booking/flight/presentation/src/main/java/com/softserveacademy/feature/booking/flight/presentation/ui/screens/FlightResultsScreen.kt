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
 * Orchestrates navigation between multiple flight segments and connects the UI with domain logic.
 *
 * @param viewModel State holder that manages segment selection, search results, and persistence.
 * @param onNext Navigation callback triggered when all segments have been selected.
 * @param onBack Navigation callback to exit the results flow and return to search criteria.
 */
@Composable
fun FlightResultsScreen(
    viewModel: FlightResultsViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // Observe final navigation event (all segments completed)
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { onNext() }
    }

    // Observe back navigation to exit the flow if at the first segment
    LaunchedEffect(Unit) {
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
        currencyCode = state.currencyCode,
        exchangeRate = state.exchangeRate,
        isNextEnabled = state.selectedOfferId != null,
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
 * Stateless UI for the Flight Results list.
 * Handles the display of the loading spinner, technical errors, and the actual flight list.
 *
 * @param visibleOffers List of flight offers to display (paginated).
 * @param origin IATA code or city name of the departure.
 * @param destination IATA code or city name of the arrival.
 * @param passengerCount Total number of travelers.
 * @param totalAvailable Count of all matching flights from the server.
 * @param isLoading Whether the data is being fetched.
 * @param error Resource ID of the error message, if any.
 * @param isNextEnabled Whether the 'Next' button should be clickable (requires a selection).
 * @param currentSegmentIndex The 0-based index of the segment being selected.
 * @param totalSegments Total segments in the booking draft.
 * @param selectedOfferId ID of the currently selected offer for highlighting.
 * @param currencyCode Currency to display (e.g. "USD").
 * @param exchangeRate Conversion rate to apply to prices.
 * @param onNext Triggered when confirming the current selection.
 * @param onBack Triggered when navigating back to a previous segment or search.
 * @param onLoadMore Triggered when clicking the pagination button.
 * @param onRetry Triggered when attempting to recover from a search error.
 * @param onFlightSelected Triggered when a user picks a flight from the list.
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
                    modifier = Modifier.padding(TravelinDimens.PaddingSmall)
                ) {
                    Icon(
                        imageVector = ArrowLeftIcon,
                        contentDescription = stringResource(R.string.flight_back_content_description),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            bottomBar = {
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

                    // Header: Flight progress and summary
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

                    // Main Content: Error, Empty, or Results List
                    when {
                        error != null -> {
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
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                itemsIndexed(visibleOffers) { index, offer ->
                                    FlightResultItem(
                                        offer = offer,
                                        isSelected = offer.id == selectedOfferId,
                                        currencyCode = currencyCode,
                                        exchangeRate = exchangeRate,
                                        onClick = { onFlightSelected(offer.id) }
                                    )
                                    if (index < visibleOffers.lastIndex) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = TravelinDimens.PaddingMedium),
                                            thickness = 1.dp,
                                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                        )
                                    }
                                }
                                // Pagination Button
                                if (totalAvailable > visibleOffers.size) {
                                    item {
                                        val remaining = totalAvailable - visibleOffers.size
                                        OutlinedButton(
                                            onClick = onLoadMore,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(TravelinDimens.PaddingMedium),
                                            shape = RoundedCornerShape(TravelinDimens.SpaceSmall),
                                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.flight_result_see_more_button, remaining),
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

// --- PREVIEWS ---

@Preview(name = "Results - Light Mode", showBackground = true)
@Composable
fun FlightResultsPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        FlightResultsContent(
            visibleOffers = emptyList(),
            origin = "SCL",
            destination = "LIM",
            passengerCount = 2,
            totalAvailable = 20,
            isLoading = false,
            error = null,
            currentSegmentIndex = 0,
            totalSegments = 2,
            selectedOfferId = null,
            currencyCode = "USD",
            exchangeRate = 1.0,
            isNextEnabled = false,
            onNext = {},
            onBack = {},
            onLoadMore = {},
            onRetry = {},
            onFlightSelected = {}
        )
    }
}