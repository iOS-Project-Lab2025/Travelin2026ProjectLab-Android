package com.softserveacademy.feature.booking.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Composable that represents the hotel booking search screen.
 *
 * @param modifier The modifier to be applied to the screen.
 * @param onBackClick The callback to be invoked when the back button is clicked.
 * @param viewModel The view model for the hotel booking search screen.
 */
@Composable
fun HotelBookingSearchScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: HotelBookingSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    TravelBookingSearchScreenContent(
        state = uiState,
        onEvent = { event ->
            when (event) {
                TravelBookingSearchEvent.OnBackClick -> onBackClick()
                else -> viewModel.onEvent(event)
            }
        },
        modifier = modifier
    )
}
