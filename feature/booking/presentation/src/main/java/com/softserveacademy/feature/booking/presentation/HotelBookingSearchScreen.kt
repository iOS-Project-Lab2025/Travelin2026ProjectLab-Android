package com.softserveacademy.feature.booking.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Composable that represents the hotel booking search screen.
 *
 * @param modifier The modifier to be applied to the screen.
 * @param onBackClick The callback to be invoked when the back button is clicked.
 * @param onNavigateToRoomSelection The callback to be invoked to navigate to the room selection screen.
 * @param viewModel The view model for the hotel booking search screen.
 */
@Composable
fun HotelBookingSearchScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNavigateToRoomSelection: () -> Unit = {},
    viewModel: HotelBookingSearchViewModel = hiltViewModel()
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

    TravelBookingSearchScreenContent(
        state = uiState,
        onEvent = { event ->
            when (event) {
                TravelBookingSearchEvent.OnBackClick -> onBackClick()
                TravelBookingSearchEvent.OnAcceptGuests -> viewModel.onEvent(event)
                else -> viewModel.onEvent(event)
            }
        },
        modifier = modifier
    )
}
