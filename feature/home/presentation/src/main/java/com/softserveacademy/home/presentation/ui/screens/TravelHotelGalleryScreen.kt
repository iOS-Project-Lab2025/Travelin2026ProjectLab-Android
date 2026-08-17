package com.softserveacademy.home.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softserveacademy.core.presentation.design_system.components.HotelGalleryScreen
import com.softserveacademy.core.presentation.design_system.components.TravelLoadingScreen
import com.softserveacademy.core.presentation.design_system.components.TravelPhotoViewer
import com.softserveacademy.home.presentation.events.HotelDetailsEvent
import com.softserveacademy.home.presentation.ui.components.detailsScreenComponents.TravelDetailsTopIcons
import com.softserveacademy.home.presentation.events.HotelDetailsEventEffect
import com.softserveacademy.home.presentation.viewmodel.HotelDetailsViewModel

/**
 * Screen that displays the full gallery of a hotel's photos.
 *
 * @param onBackClick Action to perform when the back button is clicked.
 * @param viewModel ViewModel providing the hotel data.
 */
@Composable
fun TravelHotelGalleryScreen(
    hotelId: String,
    onBackClick: () -> Unit,
    viewModel: HotelDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.hotelDetailsState.collectAsState()
    var isPhotoViewerOpen by remember { mutableStateOf(false) }
    var selectedImageIndex by remember { mutableIntStateOf(0) }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(hotelId) {
        viewModel.onEvent(HotelDetailsEvent.Load(hotelId))
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    HotelDetailsEventEffect.NavigateBack -> onBackClick()
                    else -> { /* Other effects not relevant for gallery */ }
                }
            }
        }
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    TravelLoadingScreen()
                }
                state.errorMessage != null -> {
                    Text(
                        text = "Error: ${state.errorMessage}",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.hotel != null -> {
                    val images = state.hotel!!.imageList
                    HotelGalleryScreen(
                        images = images,
                        onImageClick = { index ->
                            selectedImageIndex = index
                            isPhotoViewerOpen = true
                        }
                    )
                    
                    if (isPhotoViewerOpen) {
                        TravelPhotoViewer(
                            images = images,
                            initialIndex = selectedImageIndex
                        )
                    }
                }
            }
            
            // Reusing the top icons for consistency, primarily for the back button.
            TravelDetailsTopIcons(
                onBackClick = {
                    if (isPhotoViewerOpen) {
                        isPhotoViewerOpen = false
                    } else {
                        viewModel.onEvent(HotelDetailsEvent.NavigateBack)
                    }
                },
                onShareClick = {},
                onFavoriteClick = {}
            )
        }
    }
}
