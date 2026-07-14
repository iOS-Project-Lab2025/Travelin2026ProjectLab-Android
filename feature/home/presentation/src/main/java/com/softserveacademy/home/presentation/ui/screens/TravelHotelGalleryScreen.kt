package com.softserveacademy.home.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.core.presentation.design_system.components.HotelGalleryScreen
import com.softserveacademy.home.presentation.state.HotelDetailState
import com.softserveacademy.home.presentation.ui.components.TravelHotelDetailsTopIcons
import com.softserveacademy.home.presentation.viewmodel.HotelDetailsViewModel

/**
 * Screen that displays the full gallery of a hotel's photos.
 *
 * @param onBackClick Action to perform when the back button is clicked.
 * @param viewModel ViewModel providing the hotel data.
 */
@Composable
fun TravelHotelGalleryScreen(
    hotelId: Int,
    onBackClick: () -> Unit,
    viewModel: HotelDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.hotelDetailState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getHotelDetail(hotelId)
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (state) {
                is HotelDetailState.Data -> {
                    HotelGalleryScreen(
                        images = (state as HotelDetailState.Data).hotelDetail.imageList
                    )
                }
                is HotelDetailState.Error -> {
                    Text(
                        text = "Error: ${(state as HotelDetailState.Error).message}",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is HotelDetailState.IsLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            
            // Reusing the top icons for consistency, primarily for the back button.
            TravelHotelDetailsTopIcons(
                onBackClick = onBackClick,
                onShareClick = {},
                onFavoriteClick = {}
            )
        }
    }
}
