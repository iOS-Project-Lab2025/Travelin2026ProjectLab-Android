package com.softserveacademy.feature.booking.hotel.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.collectLatest
import com.softserveacademy.feature.booking.hotel.presentation.viewmodel.HotelContactInfoViewModel
import com.softserveacademy.feature.booking.common.presentation.events.TravelBookingContactInfoEvent
import com.softserveacademy.feature.booking.common.presentation.ui.screens.TravelBookingContactInfoScreen
import com.softserveacademy.feature.booking.hotel.presentation.R

@Composable
fun HotelContactInfoScreen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    viewModel: HotelContactInfoViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.validationSuccess) {
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
                TravelBookingContactInfoEvent.OnNextClick -> viewModel.onEvent(event)
                else -> viewModel.onEvent(event)
            }
        },
        onBackClick = onBackClick,
        modifier = modifier,
        subtitle = stringResource(R.string.contact_info_who_check_in)
    )
}