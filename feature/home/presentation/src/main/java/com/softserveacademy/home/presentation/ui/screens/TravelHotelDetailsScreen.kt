package com.softserveacademy.home.presentation.ui.screens


import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Modifier
import com.softserveacademy.home.presentation.R
import com.softserveacademy.home.presentation.events.HotelDetailsEvent
import com.softserveacademy.core.presentation.design_system.components.HotelDetailLoading
import com.softserveacademy.home.presentation.viewmodel.HotelDetailsViewModel
import com.softserveacademy.core.presentation.design_system.components.TravelDetailsScreen
import com.softserveacademy.core.presentation.design_system.components.TravelHotelDetailError
import com.softserveacademy.core.presentation.design_system.theme.LocalIsDarkTheme
import com.softserveacademy.home.presentation.ui.components.toDestinationDetails

/**
 * Stateful wrapper for the [TravelDetailsScreen].
 *
 * This composable handles the connection between the UI and the [HotelDetailsViewModel].
 * It collects the state from the ViewModel and displays the appropriate UI (Loading, Error,
 * or Data).
 *
 * @param onBackClick Action to perform when the back button is clicked.
 * @param modifier The modifier to be applied to the layout.
 * @param viewModel The ViewModel that provides the hotel detail data.
 */
@Composable
fun HotelDetailState(
    hotelId: Int,
    onBackClick: () -> Unit,
    onSeeAllPhotosClick: () -> Unit,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HotelDetailsViewModel = hiltViewModel(),
){
    val hotelDetailState by viewModel.hotelDetailState.collectAsState()
    val context = LocalContext.current
    val isDark = LocalIsDarkTheme.current
    val hotelDetails = hotelDetailState.hotelDetails

    LaunchedEffect(Unit){
        viewModel.onEvent(HotelDetailsEvent.Load(hotelId))
    }

    when {
        hotelDetailState.isLoading || (hotelDetails == null && hotelDetailState.errorMessage == null) -> {
            HotelDetailLoading()
        }
        hotelDetailState.errorMessage != null -> {
            TravelHotelDetailError(
                message = hotelDetailState.errorMessage,
                onRetry = { viewModel.onEvent(HotelDetailsEvent.Load(hotelId)) }
            )
        }
        hotelDetails != null -> {
            val shareMessage = stringResource(
                id = R.string.share_hotel_message,
                hotelDetails.name,
                hotelDetails.id
            )
            val shareTitle = stringResource(id = R.string.share_hotel_title)



            TravelDetailsScreen(
                destinationDetails = hotelDetails.toDestinationDetails(),
                isDarkTheme = isDark,
                isDescriptionExpanded = hotelDetailState.isDescriptionExpanded,
                showAmenitiesDialog = hotelDetailState.showAmenitiesDialog,
                showFullMap = hotelDetailState.showFullMap,
                onBackClick = onBackClick,
                onSeeAllPhotosClick = onSeeAllPhotosClick,
                onBookClick = onBookClick,
                onShareClick = {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareMessage)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, shareTitle)
                    context.startActivity(shareIntent)
                },
                onFavoriteClick = {
                    viewModel.onEvent(HotelDetailsEvent.ToggleFavorite)
                },
                onDescriptionExpandClick = {
                    viewModel.onEvent(HotelDetailsEvent.ToggleDescription)
                },
                onSeeAllAmenitiesClick = {
                    viewModel.onEvent(HotelDetailsEvent.ViewAllAmenities)
                },
                onDismissAmenitiesDialog = {
                    viewModel.onEvent(HotelDetailsEvent.DismissAmenities)
                },
                onMapClick = {
                    viewModel.onEvent(HotelDetailsEvent.ViewFullMap)
                },
                onDismissMap = {
                    viewModel.onEvent(HotelDetailsEvent.DismissMap)
                },
                modifier = modifier
            )
        }
    }
}
