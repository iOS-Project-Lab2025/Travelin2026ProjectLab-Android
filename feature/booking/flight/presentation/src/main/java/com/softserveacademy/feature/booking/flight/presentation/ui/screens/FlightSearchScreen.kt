package com.softserveacademy.feature.booking.flight.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.key
import com.softserveacademy.core.domain.model.FlightType
import com.softserveacademy.core.presentation.design_system.components.InlineErrorBanner
import com.softserveacademy.core.presentation.design_system.components.TravelPrimaryButton
import com.softserveacademy.core.presentation.design_system.theme.*
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent
import com.softserveacademy.feature.booking.common.presentation.ui.components.*
import com.softserveacademy.feature.booking.flight.presentation.R
import com.softserveacademy.feature.booking.flight.presentation.events.FlightSearchEvent
import com.softserveacademy.feature.booking.flight.presentation.states.FlightSearchState
import com.softserveacademy.feature.booking.flight.presentation.ui.components.CabinClassBottomSheet
import com.softserveacademy.feature.booking.flight.presentation.ui.components.FlightSegmentList
import com.softserveacademy.feature.booking.flight.presentation.ui.components.PassengerSelectionSheet
import com.softserveacademy.feature.booking.flight.presentation.ui.mappers.toDisplayName
import com.softserveacademy.feature.booking.flight.presentation.ui.mappers.toIcon
import com.softserveacademy.feature.booking.flight.presentation.ui.mappers.toMessage
import com.softserveacademy.feature.booking.flight.presentation.util.rememberFlightDateFormatter
import com.softserveacademy.feature.booking.flight.presentation.viewmodel.FlightSearchViewModel
import java.util.*

/**
 * Entry point (Stateful) for the Flight Search criteria screen.
 * Orchestrates the connection between [FlightSearchViewModel] and the UI.
 *
 * @param viewModel The view model that holds search criteria and validation logic.
 * @param onSearchExecuted Callback triggered when search is successful and ready to navigate to results.
 * @param onBack Callback to navigate back to the previous screen.
 */
@Composable
fun FlightSearchScreen(
    viewModel: FlightSearchViewModel,
    onSearchExecuted: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // Observe navigation events from the ViewModel
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { onSearchExecuted() }
    }

    FlightCriteriaContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack
    )
}

/**
 * Main container (Stateless) that organizes the screen sections.
 * Displays title, flight type chips, segment inputs, and passenger preferences.
 *
 * @param state Current UI state for search criteria.
 * @param onEvent Lambda to handle user interactions via events.
 * @param onBack Callback for the top bar's back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightCriteriaContent(
    state: FlightSearchState,
    onEvent: (FlightSearchEvent) -> Unit,
    onBack: () -> Unit
) {
    // Reference time for minimum selectable date (Today)
    val todayStartUtc = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    // State for single-date selection (One-way or Multi-city tranches)
    val singleDatePickerState = key(state.activeSegmentIndex) {
        val minDateAllowed = if (state.activeSegmentIndex > 0) {
            state.segments.getOrNull(state.activeSegmentIndex - 1)?.dateMillis ?: todayStartUtc
        } else {
            todayStartUtc
        }

        rememberDatePickerState(
            initialSelectedDateMillis = state.segments.getOrNull(state.activeSegmentIndex)?.dateMillis,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                    utcTimeMillis >= minDateAllowed
            }
        )
    }

    // State for range-date selection (Round trip)
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = state.bookingDetailsState.startDateMillis,
        initialSelectedEndDateMillis = state.bookingDetailsState.endDateMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= todayStartUtc
            }
        }
    )

    // Sync UI state with Date Picker when state changes externally
    LaunchedEffect(
        state.bookingDetailsState.startDateMillis,
        state.bookingDetailsState.endDateMillis
    ) {
        val currentStart = state.bookingDetailsState.startDateMillis
        val currentEnd = state.bookingDetailsState.endDateMillis
        if (currentStart != dateRangePickerState.selectedStartDateMillis ||
            currentEnd != dateRangePickerState.selectedEndDateMillis
        ) {
            dateRangePickerState.setSelection(currentStart, currentEnd)
        }
    }

    // Handle range selection event
    LaunchedEffect(
        dateRangePickerState.selectedStartDateMillis,
        dateRangePickerState.selectedEndDateMillis
    ) {
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
                    contentDescription = stringResource(R.string.flight_back_content_description),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        bottomBar = {
            TravelBookingBottomBar(
                onBackClick = onBack,
                onNextClick = { onEvent(FlightSearchEvent.OnPerformSearch) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Column(modifier = Modifier.padding(TravelinDimens.PaddingMedium)) {
                        FlightSearchHeader(state)
                        Spacer(Modifier.height(TravelinDimens.SpaceLarge))

                        FlightTypeSelector(state, onEvent)
                        Spacer(Modifier.height(TravelinDimens.SpaceSmall))

                        FlightSegmentList(state, onEvent)
                        FlightPreferencesSection(state, onEvent)
                    }
                }
            }
        }
    }

    // Modal Components
    CabinClassBottomSheet(state, onEvent)
    PassengerSelectionSheet(state, onEvent)

    // Unified Date Picker Modal
    if (state.showDatePicker) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = { onEvent(FlightSearchEvent.OnDismissDatePicker) },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            modifier = Modifier.padding(TravelinDimens.PaddingMedium).fillMaxSize()
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()) {

                Text(
                    text = stringResource(R.string.flight_search_date),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(TravelinDimens.PaddingMedium)
                )

                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()) {
                    if (state.selectedFlightType == FlightType.ROUND_TRIP) {
                        TravelBookingDateRangePicker(
                            title = "",
                            state = dateRangePickerState,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        DatePicker(
                            state = singleDatePickerState,
                            title = null,
                            headline = null,
                            showModeToggle = false,
                            modifier = Modifier.fillMaxWidth(),
                            colors = DatePickerDefaults.colors(containerColor = Color.Transparent)
                        )
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = TravelinDimens.ElevationSmall,
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.padding(TravelinDimens.PaddingMedium)) {
                        TravelPrimaryButton(
                            text = stringResource(com.softserveacademy.feature.booking.common.presentation.R.string.accept_button_label),
                            onClick = {
                                if (state.selectedFlightType == FlightType.ROUND_TRIP) {
                                    onEvent(
                                        FlightSearchEvent.OnDateSelected(
                                            0,
                                            dateRangePickerState.selectedStartDateMillis
                                        )
                                    )
                                } else {
                                    val index = if (state.selectedFlightType == FlightType.MULTI_CITY) state.activeSegmentIndex else 0
                                    onEvent(
                                        FlightSearchEvent.OnDateSelected(
                                            index,
                                            singleDatePickerState.selectedDateMillis
                                        )
                                    )
                                }
                                onEvent(FlightSearchEvent.OnDismissDatePicker)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

/**
 * Renders the screen title and global error banners.
 */
@Composable
private fun FlightSearchHeader(state: FlightSearchState) {
    InlineErrorBanner(
        message = stringResource(state.errorMessage ?: R.string.flight_error_generic),
        isVisible = state.errorMessage != null,
        modifier = Modifier.padding(bottom = TravelinDimens.PaddingSmall)
    )

    Text(
        text = stringResource(R.string.flight_search_title),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

/**
 * Renders Chip-based selector for [FlightType] options.
 */
@Composable
private fun FlightTypeSelector(state: FlightSearchState, onEvent: (FlightSearchEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
    ) {
        FlightType.entries.forEach { type ->
            val isSelected = state.selectedFlightType == type
            FilterChip(
                selected = isSelected,
                onClick = { onEvent(FlightSearchEvent.OnFlightTypeSelected(type)) },
                label = {
                    Text(
                        text = type.name.lowercase().replace("_", " ")
                            .replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

/**
 * Section that handles preferences like Cabin Class, Passengers, and Dates for One-way/Round-trip.
 */
@Composable
private fun FlightPreferencesSection(
    state: FlightSearchState,
    onEvent: (FlightSearchEvent) -> Unit
) {
    val dateErrorEnum = state.globalDateError ?: state.errors[0]?.dateError
    val hasDateError = dateErrorEnum != null

    Column(verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)) {
        // Cabin Class Selector
        Surface(
            onClick = { onEvent(FlightSearchEvent.OnShowCabinSheet) },
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .padding(TravelinDimens.PaddingNormal)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    state.selectedCabinClass.toIcon(),
                    null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(TravelinDimens.IconSizeLarge)
                )
                Spacer(Modifier.width(TravelinDimens.SpaceMedium))
                Text(
                    text = state.selectedCabinClass.toDisplayName(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Passenger Selector
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
                Spacer(Modifier.width(TravelinDimens.SpaceMedium))
                val totalPax = state.adults + state.children + state.infants
                Text(
                    text = pluralStringResource(R.plurals.flight_passenger_count, totalPax, totalPax),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Date Selection Card (Only for One-way/Round-trip, Multi-city uses segment pickers)
        if (state.selectedFlightType != FlightType.MULTI_CITY) {
            Column {
                Surface(
                    onClick = { onEvent(FlightSearchEvent.OnShowDatePicker) },
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(
                        width = if (hasDateError) 2.dp else 1.dp,
                        color = if (hasDateError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant
                    ),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .padding(TravelinDimens.PaddingMedium)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(CalendarIcon, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(TravelinDimens.SpaceMedium))
                        val formatter = rememberFlightDateFormatter()
                        val segmentDate = state.segments.getOrNull(0)?.dateMillis
                        val startDate = if (state.selectedFlightType == FlightType.ROUND_TRIP) state.bookingDetailsState.startDateMillis else segmentDate
                        val endDate = state.bookingDetailsState.endDateMillis

                        val dateText = when {
                            state.selectedFlightType == FlightType.ROUND_TRIP && startDate != null && endDate != null -> {
                                "${formatter.format(Date(startDate))} - ${formatter.format(Date(endDate))}"
                            }
                            startDate != null -> formatter.format(Date(startDate))
                            else -> stringResource(R.string.flight_search_date)
                        }

                        Text(
                            text = dateText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (hasDateError) MaterialTheme.colorScheme.error
                            else if (startDate != null) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (hasDateError) {
                    Text(
                        text = dateErrorEnum.toMessage(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = TravelinDimens.PaddingMedium, top = TravelinDimens.Padding2ExtraSmall)
                    )
                }
            }
        }
    }
}

// --- PREVIEWS (LIGHT & DARK MODES) ---

@Preview(name = "Light Mode", showBackground = true)
@Composable
fun FlightSearchPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            FlightCriteriaContent(state = FlightSearchState(), onEvent = {}, onBack = {})
        }
    }
}

@Preview(name = "Dark Mode", showBackground = true)
@Composable
fun FlightSearchDarkPreview() {
    Travelin2026ProjectLabTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            FlightCriteriaContent(state = FlightSearchState(), onEvent = {}, onBack = {})
        }
    }
}