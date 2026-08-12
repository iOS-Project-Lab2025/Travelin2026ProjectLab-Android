package com.softserveacademy.feature.booking.flight.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.domain.model.FlightSegment
import com.softserveacademy.core.domain.model.FlightType
import com.softserveacademy.core.presentation.design_system.components.AppTextInput
import com.softserveacademy.core.presentation.design_system.theme.*
import com.softserveacademy.feature.booking.flight.presentation.R
import com.softserveacademy.feature.booking.flight.presentation.events.FlightSearchEvent
import com.softserveacademy.feature.booking.flight.presentation.states.FlightSearchState
import com.softserveacademy.feature.booking.flight.presentation.ui.mappers.toMessage
import com.softserveacademy.feature.booking.flight.presentation.util.rememberFlightDateFormatter
import java.util.Date

/**
 * Component that renders the list of flight segments for the search criteria.
 * Filter segments visibility based on [FlightType] and manages the "Add Segment" action.
 *
 * @param state The search state containing all configured segments.
 * @param onEvent Lambda to propagate addition or removal events.
 */
@Composable
fun FlightSegmentList(state: FlightSearchState, onEvent: (FlightSearchEvent) -> Unit) {
    // VISUAL FILTER: Show only the first segment for One-way/Round-trip
    val segmentsToShow = if (state.selectedFlightType == FlightType.MULTI_CITY) {
        state.segments
    } else {
        state.segments.take(1)
    }

    segmentsToShow.forEachIndexed { index, segment ->
        // Label for Multi-city steps
        if (state.selectedFlightType == FlightType.MULTI_CITY) {
            Text(
                text = stringResource(R.string.flight_label_flight, index + 1),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = TravelinDimens.PaddingSmall)
            )
        }

        FlightSegmentItem(index, segment, state, onEvent)

        // Delete button for extra Multi-city segments (minimum 2 segments required)
        if (state.selectedFlightType == FlightType.MULTI_CITY && state.segments.size > 2) {
            TextButton(onClick = { onEvent(FlightSearchEvent.OnRemoveSegment(index)) }) {
                Text(
                    text = stringResource(R.string.flight_action_delete_flight),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        Spacer(Modifier.height(TravelinDimens.SpaceSmall))
    }

    // "Add Flight" button - Visible only in Multi-city up to 4 segments total
    if (state.selectedFlightType == FlightType.MULTI_CITY && state.segments.size < 4) {
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
 * Form item for an individual flight segment.
 * Includes origin/destination inputs, swap action, and date selection for Multi-city.
 */
@Composable
fun FlightSegmentItem(
    index: Int,
    segment: FlightSegment,
    state: FlightSearchState,
    onEvent: (FlightSearchEvent) -> Unit
) {
    val segmentError = state.errors[index]
    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            // Origin Field
            Box {
                AppTextInput(
                    value = segment.origin,
                    onValueChange = { onEvent(FlightSearchEvent.OnOriginQueryChanged(index, it)) },
                    placeholder = stringResource(R.string.flight_search_from),
                    state = if (segmentError?.originError != null) com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInputState.Error else com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInputState.Normal,
                    errorMessage = segmentError?.originError?.toMessage(),
                    leadingIcon = { Icon(PlaneTakeoffIcon, null, tint = MaterialTheme.colorScheme.primary) }
                )
                // Suggestions Overlay
                if (state.activeSegmentIndex == index && state.originSuggestions.isNotEmpty()) {
                    Box(modifier = Modifier.padding(top = 56.dp).zIndex(2f)) {
                        AirportSuggestions(state.originSuggestions) {
                            onEvent(FlightSearchEvent.OnOriginSelected(index, it))
                        }
                    }
                }
            }

            Spacer(Modifier.height(TravelinDimens.SpaceExtraSmall))

            // Destination Field
            Box {
                AppTextInput(
                    value = segment.destination,
                    onValueChange = { onEvent(FlightSearchEvent.OnDestinationQueryChanged(index, it)) },
                    placeholder = stringResource(R.string.flight_search_to),
                    state = if (segmentError?.destinationError != null) com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInputState.Error else com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInputState.Normal,
                    errorMessage = segmentError?.destinationError?.toMessage(),
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

        // Swap Locations Button
        IconButton(
            onClick = { onEvent(FlightSearchEvent.OnSwapSegmentLocations(index)) },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = TravelinDimens.PaddingSmall)
                .offset(y = -(TravelinDimens.PaddingExtraSmall + 1.dp))
                .zIndex(1f)
        ) {
            Icon(
                imageVector = SwapVerticalIcon,
                contentDescription = "Swap Locations",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(TravelinDimens.IconSizeExtraLarge)
                    .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
                    .padding(TravelinDimens.PaddingExtraSmall)
            )
        }
    }

    // Segment Date Selection (Inline for Multi-city)
    if (state.selectedFlightType == FlightType.MULTI_CITY) {
        DatePickerField(
            label = stringResource(R.string.flight_select_date),
            dateMillis = segment.dateMillis,
            isError = segmentError?.dateError != null,
            onClick = {
                onEvent(FlightSearchEvent.OnOriginQueryChanged(index, segment.origin))
                onEvent(FlightSearchEvent.OnShowDatePicker)
            }
        )
        if (segmentError?.dateError != null) {
            Text(
                text = segmentError.dateError!!.toMessage(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = TravelinDimens.PaddingMedium, top = TravelinDimens.Padding2ExtraSmall)
            )
        }
    }
}

/**
 * Dropdown list displaying airport matching suggestions.
 */
@Composable
fun AirportSuggestions(list: List<Airport>, onSelected: (Airport) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().zIndex(1f),
        elevation = CardDefaults.cardElevation(TravelinDimens.ElevationMedium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            list.forEach { airport ->
                Text(
                    text = "${airport.code} - ${airport.city}, ${airport.country}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelected(airport) }
                        .padding(TravelinDimens.PaddingNormal),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Interactive surface that triggers the date picker modal.
 */
@Composable
fun DatePickerField(label: String, dateMillis: Long?, isError: Boolean = false, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(
            width = if (isError) 2.dp else 1.dp,
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant
        ),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth().padding(vertical = TravelinDimens.Padding2ExtraSmall)
    ) {
        Row(
            modifier = Modifier.padding(TravelinDimens.PaddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = CalendarIcon,
                contentDescription = null,
                tint = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(TravelinDimens.SpaceMedium))
            val formatter = rememberFlightDateFormatter()

            Text(
                text = if (dateMillis != null) formatter.format(Date(dateMillis)) else label,
                style = MaterialTheme.typography.bodyLarge,
                color = when {
                    isError -> MaterialTheme.colorScheme.error
                    dateMillis != null -> MaterialTheme.colorScheme.onSurface
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}