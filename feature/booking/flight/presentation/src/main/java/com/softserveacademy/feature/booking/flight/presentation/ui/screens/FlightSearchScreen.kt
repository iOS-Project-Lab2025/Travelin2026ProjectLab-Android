package com.softserveacademy.feature.booking.flight.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.zIndex
import androidx.compose.runtime.key
import androidx.compose.ui.graphics.vector.ImageVector
import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.domain.model.CabinClass
import com.softserveacademy.core.domain.model.FlightSegment
import com.softserveacademy.core.domain.model.FlightType
import com.softserveacademy.core.presentation.design_system.components.AppTextInput
import com.softserveacademy.core.presentation.design_system.components.InlineErrorBanner
import com.softserveacademy.core.presentation.design_system.components.TravelPrimaryButton
import com.softserveacademy.core.presentation.design_system.theme.*
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent
import com.softserveacademy.feature.booking.common.presentation.ui.components.*
import com.softserveacademy.feature.booking.common.presentation.ui.components.util.TravelBookingCountItem
import com.softserveacademy.feature.booking.flight.presentation.R
import com.softserveacademy.feature.booking.flight.presentation.events.FlightSearchEvent
import com.softserveacademy.feature.booking.flight.presentation.states.FlightSearchState
import com.softserveacademy.feature.booking.flight.presentation.viewmodel.FlightSearchViewModel
import java.util.*

/**
 * Main entry point (Stateful) for the flight search criteria screen.
 * Handles the connection with the ViewModel and navigation events.
 */
@Composable
fun FlightSearchScreen(
    viewModel: FlightSearchViewModel,
    onSearchExecuted: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // Observe successful search to navigate
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
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightCriteriaContent(
    state: FlightSearchState,
    onEvent: (FlightSearchEvent) -> Unit,
    onBack: () -> Unit
) {
    // Current UTC time for date blocking
    val todayStartUtc = remember {
        Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    val singleDatePickerState = key(state.activeSegmentIndex) {
        rememberDatePickerState(
            initialSelectedDateMillis = state.segments.getOrNull(state.activeSegmentIndex)?.dateMillis,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis >= todayStartUtc
            }
        )
    }

    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = state.bookingDetailsState.startDateMillis,
        initialSelectedEndDateMillis = state.bookingDetailsState.endDateMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= todayStartUtc
            }
        }
    )

    // Synchronize UI state with Date Picker
    LaunchedEffect(state.bookingDetailsState.startDateMillis, state.bookingDetailsState.endDateMillis) {
        val currentStart = state.bookingDetailsState.startDateMillis
        val currentEnd = state.bookingDetailsState.endDateMillis
        if (currentStart != dateRangePickerState.selectedStartDateMillis ||
            currentEnd != dateRangePickerState.selectedEndDateMillis
        ) {
            dateRangePickerState.setSelection(currentStart, currentEnd)
        }
    }

    // Emit event when user selects a date range
    LaunchedEffect(dateRangePickerState.selectedStartDateMillis, dateRangePickerState.selectedEndDateMillis) {
        onEvent(FlightSearchEvent.InternalBookingEvent(
            TravelEnterBookingDetailsEvent.OnDateRangeSelected(
                dateRangePickerState.selectedStartDateMillis,
                dateRangePickerState.selectedEndDateMillis
            )
        ))
    }

    Scaffold(
        topBar = {
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(start = TravelinDimens.PaddingSmall, top = TravelinDimens.PaddingSmall)
            ) {
                Icon(
                    imageVector = ArrowLeftIcon,
                    contentDescription = "Back",
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
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
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

    // Sheets and Modals
    CabinClassBottomSheet(state, onEvent)
    PassengerSelectionSheet(state, onEvent)

    // --- UNIFIED DATE MODAL (Figma & Professional Style) ---
    if (state.showDatePicker) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = { onEvent(FlightSearchEvent.OnDismissDatePicker) },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            dragHandle = null,
            modifier = Modifier.fillMaxSize()
        ) {
            // Columna principal del modal
            Column(modifier = Modifier.fillMaxSize().navigationBarsPadding()) {

                // Título
                Text(
                    text = stringResource(R.string.flight_search_date),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(TravelinDimens.PaddingMedium)
                )

                // Calendario con contenedor flexible pero controlado
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
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
                            colors = DatePickerDefaults.colors(
                                containerColor = Color.Transparent,

                            )
                        )
                    }
                }

                // Botón Aceptar fijo abajo
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
                                    // Sincronizamos el rango al primer segmento para el Draft
                                    onEvent(FlightSearchEvent.OnDateSelected(0, dateRangePickerState.selectedStartDateMillis))
                                } else {
                                    val index = if (state.selectedFlightType == FlightType.MULTI_CITY) state.activeSegmentIndex else 0
                                    onEvent(FlightSearchEvent.OnDateSelected(index, singleDatePickerState.selectedDateMillis))
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
 * Chip selector for Round Trip, One Way or Multi-city flights.
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
                        text = type.name.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() },
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
 * Renders a list of flight segments. Supports dynamic additions for Multi-city.
 */
@Composable
private fun FlightSegmentList(state: FlightSearchState, onEvent: (FlightSearchEvent) -> Unit) {
    state.segments.forEachIndexed { index, segment ->
        if (state.selectedFlightType == FlightType.MULTI_CITY) {
            Text(
                text = stringResource(R.string.flight_label_flight, index + 1),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = TravelinDimens.PaddingSmall)
            )
        }

        FlightSegmentItem(index, segment, state, onEvent)

        if (state.selectedFlightType == FlightType.MULTI_CITY && state.segments.size > 2) {
            TextButton(onClick = { onEvent(FlightSearchEvent.OnRemoveSegment(index)) }) {
                Text(stringResource(R.string.flight_action_delete_flight), color = MaterialTheme.colorScheme.error)
            }
        }
        Spacer(Modifier.height(TravelinDimens.SpaceSmall))
    }

    if (state.selectedFlightType == FlightType.MULTI_CITY) {
        TextButton(
            onClick = { onEvent(FlightSearchEvent.OnAddSegment) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(AddIcon, null)
            Spacer(Modifier.width(TravelinDimens.SpaceSmall))
            Text(stringResource(R.string.flight_action_add_flight))
        }
        Spacer(Modifier.height(TravelinDimens.SpaceSmall))
    }
}

/**
 * Individual segment component (Origin, Destination, and Swap button).
 */
@Composable
private fun FlightSegmentItem(
    index: Int,
    segment: FlightSegment,
    state: FlightSearchState,
    onEvent: (FlightSearchEvent) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            Box {
                AppTextInput(
                    value = segment.origin,
                    onValueChange = { onEvent(FlightSearchEvent.OnOriginQueryChanged(index, it)) },
                    placeholder = stringResource(R.string.flight_search_from),
                    leadingIcon = { Icon(PlaneTakeoffIcon, null, tint = MaterialTheme.colorScheme.primary) }
                )
                if (state.activeSegmentIndex == index && state.originSuggestions.isNotEmpty()) {
                    Box(modifier = Modifier.padding(top = 56.dp).zIndex(2f)) {
                        AirportSuggestions(state.originSuggestions) {
                            onEvent(FlightSearchEvent.OnOriginSelected(index, it))
                        }
                    }
                }
            }

            Spacer(Modifier.height(TravelinDimens.SpaceExtraSmall))

            Box {
                AppTextInput(
                    value = segment.destination,
                    onValueChange = { onEvent(FlightSearchEvent.OnDestinationQueryChanged(index, it)) },
                    placeholder = stringResource(R.string.flight_search_to),
                    leadingIcon = { Icon(PlaneLandIcon, null, tint = MaterialTheme.colorScheme.primary) }
                )
                if (state.activeSegmentIndex == index && state.destinationSuggestions.isNotEmpty()) {
                    Box(modifier = Modifier.padding(top = 56.dp).zIndex(2f)) {
                        AirportSuggestions(state.destinationSuggestions) {
                            onEvent(FlightSearchEvent.OnDestinationSelected(index, it))
                        }
                    }
                }
            }
        }

        IconButton(
            onClick = { onEvent(FlightSearchEvent.OnSwapSegmentLocations(index)) },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = TravelinDimens.PaddingSmall)
                .offset(y = (-5).dp)
                .zIndex(1f)
        ) {
            Icon(
                imageVector = SwapVerticalIcon,
                contentDescription = "Swap",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(TravelinDimens.IconSizeExtraLarge)
                    .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
                    .padding(TravelinDimens.PaddingExtraSmall)
            )
        }
    }

    if (state.selectedFlightType == FlightType.MULTI_CITY) {
        DatePickerField(
            label = stringResource(R.string.flight_select_date),
            dateMillis = segment.dateMillis,
            onClick = {
                // we update the active index so modal will know to wich flight put the date
                onEvent(FlightSearchEvent.OnOriginQueryChanged(index, segment.origin))
                // open modal calendar
                onEvent(FlightSearchEvent.OnShowDatePicker)
            }
        )
    }
}

/**
 * Section for cabin type and passenger count preferences.
 */
@Composable
private fun FlightPreferencesSection(state: FlightSearchState, onEvent: (FlightSearchEvent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)) {
        Surface(
            onClick = { onEvent(FlightSearchEvent.OnShowCabinSheet) },
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier.padding(TravelinDimens.PaddingMedium).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(state.selectedCabinClass.toIcon(), null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(TravelinDimens.SpaceMedium))
                Text(
                    text = state.selectedCabinClass.toDisplayName(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Surface(
            onClick = { onEvent(FlightSearchEvent.OnShowPassengerSheet) },
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier.padding(TravelinDimens.PaddingMedium).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(PersonsIcon, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(TravelinDimens.SpaceMedium))
                val totalPax = state.adults + state.children + state.infants
                Text(
                    text = pluralStringResource(
                        R.plurals.flight_passenger_count,
                        totalPax,
                        totalPax
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        // --- DATE SELECTION CARD ---
        if (state.selectedFlightType != FlightType.MULTI_CITY) {
            Surface(
                onClick = { onEvent(FlightSearchEvent.OnShowDatePicker) },
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier.padding(TravelinDimens.PaddingMedium).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(CalendarIcon, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(12.dp))
                    val formatter = rememberDateFormatter()
                    // Leemos la fecha del primer segmento para One Way y Multi-city
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
                        color = if (startDate != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Bottom sheet for selecting cabin class with high-contrast highlighted items.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CabinClassBottomSheet(state: FlightSearchState, onEvent: (FlightSearchEvent) -> Unit) {
    if (state.showCabinSheet) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(FlightSearchEvent.OnDismissCabinSheet) },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(TravelinDimens.PaddingMedium).fillMaxWidth().padding(bottom = TravelinDimens.PaddingExtraLarge).verticalScroll(rememberScrollState())) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.flight_cabin_class_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                }

                Spacer(Modifier.height(TravelinDimens.SpaceMedium))

                CabinClass.entries.forEach { cabin ->
                    val isSelected = state.selectedCabinClass == cabin
                    Surface(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(64.dp)
                            .clickable { onEvent(FlightSearchEvent.OnCabinClassSelected(cabin)) },
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        ),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 16.dp).fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = cabin.toIcon(),
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(TravelinDimens.IconSizeMedium)
                            )
                            Spacer(Modifier.width(TravelinDimens.SpaceMedium))
                            Text(
                                text = cabin.toDisplayName(),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Bottom sheet for selecting guest counts. Reuses common components.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PassengerSelectionSheet(state: FlightSearchState, onEvent: (FlightSearchEvent) -> Unit) {
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
            onAccept = { onEvent(FlightSearchEvent.InternalBookingEvent(TravelEnterBookingDetailsEvent.OnAcceptClick)) },
            onDismissRequest = { onEvent(FlightSearchEvent.InternalBookingEvent(TravelEnterBookingDetailsEvent.OnDismissBottomSheet)) },
            title = stringResource(R.string.flight_passengers_title),
            subtitle = stringResource(R.string.flight_passengers_subtitle)
        )
    }
}

/**
 * Helper component for airport auto-suggestions.
 */
@Composable
private fun AirportSuggestions(list: List<Airport>, onSelected: (Airport) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().zIndex(1f),
        elevation = CardDefaults.cardElevation(TravelinDimens.ElevationMedium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        list.forEach { airport ->
            Text(
                text = "${airport.code} - ${airport.city}, ${airport.country}",
                modifier = Modifier.fillMaxWidth().clickable { onSelected(airport) }.padding(TravelinDimens.PaddingNormal),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Styled text field that acts as a button to trigger a date picker.
 */
@Composable
fun DatePickerField(label: String, dateMillis: Long?, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth().padding(vertical = TravelinDimens.Padding2ExtraSmall)
    ) {
        Row(modifier = Modifier.padding(TravelinDimens.PaddingMedium), verticalAlignment = Alignment.CenterVertically) {
            Icon(CalendarIcon, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(TravelinDimens.SpaceMedium))
            val formatter = rememberDateFormatter()

            Text(
                text = if (dateMillis != null) {
                    formatter.format(Date(dateMillis))
                } else label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (dateMillis != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Formats a timestamp into a readable date string including the year.
 * Example: Aug 12, 2026
 */
@Composable
private fun rememberDateFormatter(): java.text.SimpleDateFormat {
    return remember { java.text.SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
}

/**
 * Maps a CabinClass to its corresponding design system icon.
 */
@Composable
private fun CabinClass.toIcon(): ImageVector {
    return when (this) {
        CabinClass.ECONOMY -> EconomyClassIcon
        CabinClass.PREMIUM_ECONOMY -> PremiumEconomyClassIcon
        CabinClass.BUSINESS -> BusinessClassIcon
        CabinClass.FIRST -> FirstClassIcon
    }
}

/**
 * Maps a CabinClass to its localized display name.
 */
@Composable
private fun CabinClass.toDisplayName(): String {
    return when (this) {
        CabinClass.ECONOMY -> stringResource(R.string.flight_cabin_economy)
        CabinClass.PREMIUM_ECONOMY -> stringResource(R.string.flight_cabin_premium_economy)
        CabinClass.BUSINESS -> stringResource(R.string.flight_cabin_business)
        CabinClass.FIRST -> stringResource(R.string.flight_cabin_first)
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