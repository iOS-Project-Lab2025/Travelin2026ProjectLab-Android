package com.softserveacademy.feature.booking.flight.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.presentation.design_system.components.AppTextInput
import com.softserveacademy.core.presentation.design_system.components.InlineErrorBanner
import com.softserveacademy.core.presentation.design_system.theme.*
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent
import com.softserveacademy.feature.booking.common.presentation.ui.components.*
import com.softserveacademy.feature.booking.common.presentation.ui.components.util.TravelBookingCountItem
import com.softserveacademy.feature.booking.flight.presentation.R
import com.softserveacademy.feature.booking.flight.presentation.events.FlightSearchEvent
import com.softserveacademy.feature.booking.flight.presentation.states.FlightSearchState
import com.softserveacademy.feature.booking.flight.presentation.viewmodel.FlightSearchViewModel
import java.util.Calendar
import java.util.TimeZone

@Composable
fun FlightSearchScreen(
    viewModel: FlightSearchViewModel,
    onSearchExecuted: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { onSearchExecuted() }
    }

    FlightCriteriaContent(state, viewModel::onEvent, onBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightCriteriaContent(
    state: FlightSearchState,
    onEvent: (FlightSearchEvent) -> Unit,
    onBack: () -> Unit
) {
    val todayStartUtc = remember {
        Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    // Date Range Picker State needed for the Common component
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = state.bookingDetailsState.startDateMillis,
        initialSelectedEndDateMillis = state.bookingDetailsState.endDateMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Only allow dates from today onwards
                return utcTimeMillis >= todayStartUtc
            }
        }
    )

    LaunchedEffect(state.bookingDetailsState.startDateMillis, state.bookingDetailsState.endDateMillis) {
        val currentStart = state.bookingDetailsState.startDateMillis
        val currentEnd = state.bookingDetailsState.endDateMillis
        if (currentStart != dateRangePickerState.selectedStartDateMillis ||
            currentEnd != dateRangePickerState.selectedEndDateMillis
        ) {
            dateRangePickerState.setSelection(currentStart, currentEnd)
        }
    }

    LaunchedEffect(dateRangePickerState.selectedStartDateMillis, dateRangePickerState.selectedEndDateMillis) {
        onEvent(
            FlightSearchEvent.InternalBookingEvent(
                TravelEnterBookingDetailsEvent.OnDateRangeSelected(
                    dateRangePickerState.selectedStartDateMillis,
                    dateRangePickerState.selectedEndDateMillis
                )
            )
        )
    }

    Scaffold(
        bottomBar = {
            TravelBookingBottomBar(
                onBackClick = onBack,
                onNextClick = { onEvent(FlightSearchEvent.OnPerformSearch) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        // 2. Usamos un Box para permitir que las sugerencias floten (Z-Index)
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {

                // --- SECTION: "THE WHERE" ---
                item {
                    Column(modifier = Modifier.padding(TravelinDimens.PaddingMedium)) {
                        InlineErrorBanner(
                            message = stringResource(
                                state.errorMessage ?: R.string.flight_error_generic
                            ),
                            isVisible = state.errorMessage != null,
                            modifier = Modifier.padding(bottom = TravelinDimens.PaddingSmall)
                        )

                        Text(
                            text = stringResource(R.string.flight_search_title),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.height(16.dp))

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Column{
                                // Origin Input
                                AppTextInput(
                                    value = state.originQuery,
                                    onValueChange = { onEvent(FlightSearchEvent.OnOriginQueryChanged(it)) },
                                    placeholder = stringResource(R.string.flight_search_from),
                                    leadingIcon = {
                                        Icon(
                                            PlaneTakeoffIcon,
                                            null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                )

                                Spacer(Modifier.height(8.dp))

                                // Destination Input
                                AppTextInput(
                                    value = state.destinationQuery,
                                    onValueChange = { onEvent(FlightSearchEvent.OnDestinationQueryChanged(it)) },
                                    placeholder = stringResource(R.string.flight_search_to),
                                    leadingIcon = {
                                        Icon(
                                            PlaneLandIcon,
                                            null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                )

                                Spacer(Modifier.height(16.dp))

                            }

                            IconButton(
                                    onClick = { onEvent(FlightSearchEvent.OnSwapLocations) },
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(end = 8.dp)
                                        .offset(y = (-10).dp)
                                        .zIndex(1f)
                                ) {
                                    Icon(
                                        imageVector = SwapVerticalIcon, // O el icono que prefieras
                                        contentDescription = "Swap",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .size(TravelinDimens.IconSizeExtraLarge)
                                            .background(MaterialTheme.colorScheme.surface, shape = CircleShape) // 5. Fondo sólido para tapar los bordes traseros
                                            .padding(4.dp)
                                    )
                                }
                            

                        }



                        // Passenger Selection Card
                        Surface(
                            onClick = { onEvent(FlightSearchEvent.OnShowPassengerSheet) },
                            shape = MaterialTheme.shapes.medium,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(TravelinDimens.PaddingMedium)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(PersonsIcon, null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    text = "${state.adults + state.children + state.infants} Passengers",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
                item {
                    // --- SECTION: "THE WHEN" (Calendar) ---
                    TravelBookingDateRangePicker(
                        title = stringResource(R.string.flight_search_date),
                        state = dateRangePickerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(550.dp)
                    )
                }
            }


            // 3. CAPA DE SUGERENCIAS (Flotante sobre el contenido)
            // Origen
            if (state.originSuggestions.isNotEmpty()) {
                Box(modifier = Modifier.padding(top = 120.dp, start = 16.dp, end = 16.dp)) {
                    AirportSuggestions(state.originSuggestions) {
                        onEvent(
                            FlightSearchEvent.OnOriginSelected(
                                it
                            )
                        )
                    }
                }
            }
            // Destino
            if (state.destinationSuggestions.isNotEmpty()) {
                Box(modifier = Modifier.padding(top = 180.dp, start = 16.dp, end = 16.dp)) {
                    AirportSuggestions(state.destinationSuggestions) {
                        onEvent(
                            FlightSearchEvent.OnDestinationSelected(
                                it
                            )
                        )
                    }
                }
            }


            // --- SECTION 4: PASSENGER SELECTION SHEET ---
            if (state.bookingDetailsState.showGuestBottomSheet) {
                val passengerItems = listOf(
                    TravelBookingCountItem.Counter(
                        label = stringResource(R.string.flight_label_adults),
                        subtitle = stringResource(R.string.flight_subtitle_adults),
                        count = state.adults,
                        onCountChange = { onEvent(FlightSearchEvent.OnAdultsChanged(it)) },
                        minCount = 1
                    ),
                    TravelBookingCountItem.Counter(
                        label = stringResource(R.string.flight_label_children),
                        subtitle = stringResource(R.string.flight_subtitle_children),
                        count = state.children,
                        onCountChange = { onEvent(FlightSearchEvent.OnChildrenChanged(it)) }
                    ),
                    TravelBookingCountItem.Counter(
                        label = stringResource(R.string.flight_label_infants),
                        subtitle = stringResource(R.string.flight_subtitle_infants),
                        count = state.infants,
                        onCountChange = { onEvent(FlightSearchEvent.OnInfantsChanged(it)) }
                    )
                )
                TravelBookingCountSheet(
                    items = passengerItems,
                    onAccept = {
                        onEvent(
                            FlightSearchEvent.InternalBookingEvent(
                                TravelEnterBookingDetailsEvent.OnAcceptClick
                            )
                        )
                    },
                    onDismissRequest = {
                        onEvent(
                            FlightSearchEvent.InternalBookingEvent(
                                TravelEnterBookingDetailsEvent.OnDismissBottomSheet
                            )
                        )
                    },
                    title = stringResource(R.string.flight_passengers_title),
                    subtitle = stringResource(R.string.flight_passengers_subtitle)
                )
            }
        }
    }
}

@Composable
private fun AirportSuggestions(list: List<Airport>, onSelected: (Airport) -> Unit) {
    if (list.isNotEmpty()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            list.forEach { airport ->
                Text(
                    text = "${airport.code} - ${airport.city}, ${airport.country}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelected(airport) }
                        .padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

// --- Previews ---

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Search - Light", widthDp = 360, heightDp = 800)
@Composable
fun FlightSearchPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        // Surface es vital para que Compose sepa que hay un fondo sólido
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            FlightCriteriaContent(
                state = FlightSearchState(
                    originQuery = "SCL",
                    destinationQuery = "LIM",
                    // Aseguramos que el estado del calendario no sea nulo en el preview
                    bookingDetailsState = com.softserveacademy.feature.booking.common.presentation.states.TravelEnterBookingDetailsState(
                        startDateMillis = System.currentTimeMillis(),
                        endDateMillis = System.currentTimeMillis() + 86400000
                    )
                ),
                onEvent = {},
                onBack = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Search - Dark", widthDp = 360, heightDp = 800)
@Composable
fun FlightSearchDarkPreview() {
    Travelin2026ProjectLabTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            FlightCriteriaContent(
                state = FlightSearchState(
                    originQuery = "SCL",
                    destinationQuery = "LIM",
                    originSuggestions = listOf(
                        Airport("SCL", "Arturo Merino", "Santiago", "Chile")
                    )
                ),
                onEvent = {},
                onBack = {}
            )
        }
    }
}
