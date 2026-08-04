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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softserveacademy.home.presentation.R
import com.softserveacademy.home.presentation.events.HotelDetailsEvent
import com.softserveacademy.home.presentation.events.HotelDetailsEventEffect
import com.softserveacademy.core.presentation.design_system.components.HotelDetailLoading
import com.softserveacademy.home.presentation.viewmodel.HotelDetailsViewModel
import com.softserveacademy.core.presentation.design_system.components.TravelDetailsScreen
import com.softserveacademy.core.presentation.design_system.components.TravelHotelDetailError
import com.softserveacademy.core.presentation.design_system.theme.LocalIsDarkTheme
import com.softserveacademy.home.presentation.model.TravelItemType

/**
 * Stateful wrapper for the [TravelDetailsScreen].
 *
 * This composable handles the connection between the UI and the [HotelDetailsViewModel].
 * It collects the state from the ViewModel and displays the appropriate UI (Loading, Error,
 * or Data).
 *
 * @param itemId The unique identifier of the hotel or tour.
 * @param type The type of travel item (HOTEL or TOUR).
 * @param onBackClick Action to perform when the back button is clicked.
 * @param modifier The modifier to be applied to the layout.
 * @param viewModel The ViewModel that provides the hotel detail data.
 */
@Composable
fun TravelItemDetailState(
    itemId: String,
    type: TravelItemType,
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
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(itemId, type){
        viewModel.onEvent(HotelDetailsEvent.Load(itemId, type))
    }

    val shareTitle = stringResource(id = R.string.share_hotel_title)
    val shareMessageTemplate = stringResource(id = R.string.share_hotel_message)

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    HotelDetailsEventEffect.NavigateBack -> onBackClick()
                    is HotelDetailsEventEffect.NavigateToBooking -> onBookClick()
                    is HotelDetailsEventEffect.NavigateToGallery -> onSeeAllPhotosClick()
                    is HotelDetailsEventEffect.ShareHotel -> {
                        val shareMessage = shareMessageTemplate.format(
                            effect.hotel.name,
                            effect.hotel.id
                        )
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareMessage)
                            setType("text/plain")
                        }
                        val shareIntent = Intent.createChooser(sendIntent, shareTitle)
                        context.startActivity(shareIntent)
                    }
                }
            }
        }
    }

    when {
        hotelDetailState.isLoading || (hotelDetails == null && hotelDetailState.errorMessage == null) -> {
            HotelDetailLoading()
        }
        hotelDetailState.errorMessage != null -> {
            TravelHotelDetailError(
                message = hotelDetailState.errorMessage,
                onRetry = { viewModel.onEvent(HotelDetailsEvent.Load(itemId, type)) }
            )
        }
        hotelDetails != null -> {
            TravelDetailsScreen(
                destinationDetails = hotelDetails,
                isDarkTheme = isDark,
                isDescriptionExpanded = hotelDetailState.isDescriptionExpanded,
                showAmenitiesDialog = hotelDetailState.showAmenitiesDialog,
                showFullMap = hotelDetailState.showFullMap,
                showBookingBar = (type == TravelItemType.HOTEL),
                onBackClick = {
                    viewModel.onEvent(HotelDetailsEvent.NavigateBack)
                },
                onSeeAllPhotosClick = {
                    viewModel.onEvent(HotelDetailsEvent.ViewGallery)
                },
                onBookClick = {
                    viewModel.onEvent(HotelDetailsEvent.BookNow)
                },
                onShareClick = {
                    viewModel.onEvent(HotelDetailsEvent.Share)
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
