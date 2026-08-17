package com.softserveacademy.feature.booking.tour.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.feature.booking.common.presentation.events.TravelBookingContactInfoEvent
import com.softserveacademy.feature.booking.common.presentation.ui.screens.TravelBookingContactInfoScreen
import com.softserveacademy.feature.booking.tour.presentation.R
import com.softserveacademy.feature.booking.tour.presentation.viewmodel.TourContactInfoViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TourContactInfoScreen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TourContactInfoViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.validationSuccess.collectLatest { success ->
            if (success) {
                onNextClick()
                viewModel.resetValidationStatus()
            }
        }
    }

    TravelBookingContactInfoScreen(
        state = state,
        onEvent = { event ->
            when (event) {
                TravelBookingContactInfoEvent.OnBackClick -> onBackClick()
                else -> viewModel.onEvent(event)
            }
        },
        onBackClick = onBackClick,
        modifier = modifier,
        subtitle = stringResource(R.string.contact_info_subtitle)
    )
}
