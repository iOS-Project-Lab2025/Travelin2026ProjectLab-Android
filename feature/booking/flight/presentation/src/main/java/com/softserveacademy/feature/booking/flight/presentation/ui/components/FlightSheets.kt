package com.softserveacademy.feature.booking.flight.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.domain.model.CabinClass
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent
import com.softserveacademy.feature.booking.common.presentation.ui.components.TravelBookingCountSheet
import com.softserveacademy.feature.booking.common.presentation.ui.components.util.TravelBookingCountItem
import com.softserveacademy.feature.booking.flight.presentation.R
import com.softserveacademy.feature.booking.flight.presentation.events.FlightSearchEvent
import com.softserveacademy.feature.booking.flight.presentation.states.FlightSearchState
import com.softserveacademy.feature.booking.flight.presentation.ui.mappers.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CabinClassBottomSheet(state: FlightSearchState, onEvent: (FlightSearchEvent) -> Unit) {
    if (state.showCabinSheet) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(FlightSearchEvent.OnDismissCabinSheet) },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(TravelinDimens.PaddingMedium).fillMaxWidth().padding(bottom = TravelinDimens.PaddingExtraLarge).verticalScroll(rememberScrollState())) {
                Text(text = stringResource(R.string.flight_cabin_class_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
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
                            Icon(imageVector = cabin.toIcon(), contentDescription = null, tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(TravelinDimens.IconSizeLarge))
                            Spacer(Modifier.width(TravelinDimens.SpaceMedium))
                            Text(text = cabin.toDisplayName(), style = MaterialTheme.typography.bodyLarge, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassengerSelectionSheet(state: FlightSearchState, onEvent: (FlightSearchEvent) -> Unit) {
    if (state.bookingDetailsState.showGuestBottomSheet) {
        val currentTotal = state.adults + state.children + state.infants
        val remainingSpace = 9 - currentTotal
        val subtitleText = if (currentTotal >= 9)
            "Maximum of 9 passengers allowed" // Debería ir a strings.xml
        else stringResource(R.string.flight_max_passengers_subtitle)

        val passengerItems = listOf(
            TravelBookingCountItem.Counter(
                label = stringResource(R.string.flight_label_adults),
                subtitle = stringResource(R.string.flight_subtitle_adults),
                count = state.adults,
                onCountChange = { onEvent(FlightSearchEvent.OnAdultsChanged(it)) },
                minCount = 1,
                maxCount = state.adults + remainingSpace
            ),
            TravelBookingCountItem.Counter(
                label = stringResource(R.string.flight_label_children),
                subtitle = stringResource(R.string.flight_subtitle_children),
                count = state.children,
                onCountChange = { onEvent(FlightSearchEvent.OnChildrenChanged(it)) },
                maxCount = state.children + remainingSpace
            ),
            TravelBookingCountItem.Counter(
                label = stringResource(R.string.flight_label_infants),
                subtitle = stringResource(R.string.flight_subtitle_infants),
                count = state.infants,
                onCountChange = { onEvent(FlightSearchEvent.OnInfantsChanged(it)) },
                maxCount = state.infants + remainingSpace
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