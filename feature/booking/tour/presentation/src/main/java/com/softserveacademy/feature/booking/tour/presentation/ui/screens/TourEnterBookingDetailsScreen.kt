package com.softserveacademy.feature.booking.tour.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent
import com.softserveacademy.feature.booking.common.presentation.ui.components.util.TravelBookingCountItem
import com.softserveacademy.feature.booking.common.presentation.ui.screens.TravelEnterBookingDetailsScreen
import com.softserveacademy.feature.booking.tour.presentation.R
import com.softserveacademy.feature.booking.tour.presentation.viewmodel.TourEnterBookingDetailsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TourEnterBookingDetailsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNavigateToContactInfo: () -> Unit = {},
    viewModel: TourEnterBookingDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.validationSuccess.collectLatest { success ->
            if (success) {
                onNavigateToContactInfo()
                viewModel.resetValidationStatus()
            }
        }
    }

    val bookingCountItems = listOf(
        TravelBookingCountItem.Counter(
            label = stringResource(R.string.adults_label),
            count = uiState.adultsCount,
            onCountChange = { viewModel.onEvent(TravelEnterBookingDetailsEvent.OnAdultsCountChange(it)) },
            minCount = 1
        ),
        TravelBookingCountItem.Counter(
            label = stringResource(R.string.children_label),
            subtitle = stringResource(R.string.children_subtitle),
            count = uiState.childrenCount,
            onCountChange = { viewModel.onEvent(TravelEnterBookingDetailsEvent.OnChildrenCountChange(it)) }
        ),
        TravelBookingCountItem.Counter(
            label = stringResource(R.string.babies_label),
            subtitle = stringResource(R.string.babies_subtitle),
            count = uiState.babiesCount,
            onCountChange = { viewModel.onEvent(TravelEnterBookingDetailsEvent.OnBabiesCountChange(it)) }
        )
    )

    TravelEnterBookingDetailsScreen(
        modifier = modifier,
        state = uiState,
        onEvent = { event ->
            when (event) {
                TravelEnterBookingDetailsEvent.OnBackClick -> onBackClick()
                else -> viewModel.onEvent(event)
            }
        },
        bookingCountItems = bookingCountItems,
        bottomSheetTitle = stringResource(R.string.participant_count_title),
        bottomSheetSubtitle = stringResource(R.string.participant_count_subtitle)
    )
}
