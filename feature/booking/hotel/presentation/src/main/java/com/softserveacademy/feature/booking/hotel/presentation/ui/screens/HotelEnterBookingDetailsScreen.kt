package com.softserveacademy.feature.booking.hotel.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent
import com.softserveacademy.feature.booking.hotel.presentation.events.HotelEnterBookingDetailsEvent
import com.softserveacademy.feature.booking.hotel.presentation.viewmodel.HotelEnterBookingDetailsViewModel
import com.softserveacademy.feature.booking.common.presentation.ui.screens.TravelEnterBookingDetailsScreen
import com.softserveacademy.feature.booking.common.presentation.ui.components.util.TravelBookingCountItem
import com.softserveacademy.feature.booking.hotel.presentation.R
import kotlinx.coroutines.flow.collectLatest

/**
 * Stateful wrapper for [TravelEnterBookingDetailsScreen] used for the hotel booking flow.
 *
 * @param modifier The modifier to be applied to the screen.
 * @param onBackClick The callback to be invoked when the back button is clicked.
 * @param onNavigateToRoomSelection The callback to be invoked to navigate to the room selection screen.
 * @param viewModel The view model for the enter hotel booking details screen.
 */
@Composable
fun HotelEnterBookingDetailsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNavigateToRoomSelection: () -> Unit = {},
    viewModel: HotelEnterBookingDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.validationSuccess) {
        viewModel.validationSuccess.collectLatest { success ->
            if (success) {
                onNavigateToRoomSelection()
                viewModel.resetValidationStatus()
            }
        }
    }

    val bookingCountItems = listOf(
        TravelBookingCountItem.Counter(
            label = stringResource(R.string.adults_label),
            count = uiState.adultsCount,
            onCountChange = { viewModel.onEvent(HotelEnterBookingDetailsEvent.OnAdultsCountChange(it)) },
            minCount = 1
        ),
        TravelBookingCountItem.Counter(
            label = stringResource(R.string.children_label),
            subtitle = stringResource(R.string.children_subtitle),
            count = uiState.childrenCount,
            onCountChange = { viewModel.onEvent(HotelEnterBookingDetailsEvent.OnChildrenCountChange(it)) }
        ),
        TravelBookingCountItem.Switch(
            label = stringResource(R.string.pets_label),
            checked = uiState.hasPets,
            onCheckedChange = { viewModel.onEvent(HotelEnterBookingDetailsEvent.OnHasPetsChange(it)) }
        )
    )

    TravelEnterBookingDetailsScreen(
        modifier = modifier,
        state = uiState.screenState,
        onEvent = { event ->
            when (event) {
                TravelEnterBookingDetailsEvent.OnBackClick -> onBackClick()
                else -> viewModel.onEvent(HotelEnterBookingDetailsEvent.ScreenEvent(event))
            }
        },
        bookingCountItems = bookingCountItems,
        bottomSheetTitle = stringResource(R.string.guest_count_title),
        bottomSheetSubtitle = stringResource(R.string.guest_count_subtitle)
    )
}
