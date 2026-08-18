package com.softserveacademy.home.presentation.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.model.LatLng
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.Poi
import com.softserveacademy.core.domain.util.formatPrice
import com.softserveacademy.home.presentation.R
import com.softserveacademy.home.presentation.events.HotelDetailsEvent
import com.softserveacademy.home.presentation.events.HotelDetailsEventEffect
import com.softserveacademy.core.presentation.design_system.components.HotelDetailLoading
import com.softserveacademy.core.presentation.design_system.components.TravelErrorScreen
import com.softserveacademy.home.presentation.ui.components.detailsScreenComponents.TravelDetailsBottomBar
import com.softserveacademy.core.presentation.design_system.theme.LocalIsDarkTheme
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.home.presentation.ui.components.detailsScreenComponents.FeaturesOverlay
import com.softserveacademy.home.presentation.ui.components.detailsScreenComponents.ExploreAreaOverlay
import com.softserveacademy.home.presentation.ui.components.detailsScreenComponents.TravelDetailsFeaturesSection
import com.softserveacademy.home.presentation.ui.components.detailsScreenComponents.TravelDetailsDescription
import com.softserveacademy.home.presentation.ui.components.detailsScreenComponents.TravelDetailsHeader
import com.softserveacademy.home.presentation.ui.components.detailsScreenComponents.TravelDetailsMap
import com.softserveacademy.home.presentation.ui.components.detailsScreenComponents.MapOverlay
import com.softserveacademy.home.presentation.ui.components.detailsScreenComponents.NearbyPlacesSection
import com.softserveacademy.home.presentation.viewmodel.HotelDetailsViewModel

/**
 * Stateful screen that use the [TravelHotelDetailsWrapper].
 *
 * This composable handles the connection between the UI and the [HotelDetailsViewModel].
 * It collects the state from the ViewModel and displays the appropriate UI.
 *
 * @param itemId The unique identifier of the hotel or tour.
 * @param onBackClick Action to perform when the back button is clicked.
 * @param onSeeAllPhotosClick Action to perform when the "See all photos" button is clicked.
 * @param onBookClick Action to perform when the "Book Now" button is clicked.
 * @param modifier The modifier to be applied to the layout.
 * @param viewModel The ViewModel that provides the hotel detail data.
 */
@Composable
fun TravelHotelDetailScreen(
    itemId: String,
    onBackClick: () -> Unit,
    onSeeAllPhotosClick: () -> Unit,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HotelDetailsViewModel = hiltViewModel(),
){
    val hotelDetailState by viewModel.hotelDetailsState.collectAsState()
    val context = LocalContext.current
    val isDark = LocalIsDarkTheme.current
    val hotelDetails = hotelDetailState.hotel
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(itemId){
        viewModel.onEvent(HotelDetailsEvent.Load(itemId))
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
            TravelErrorScreen(
                message = hotelDetailState.errorMessage,
                onRetry = { viewModel.onEvent(HotelDetailsEvent.Load(itemId)) }
            )
        }
        hotelDetails != null -> {
            TravelHotelDetailsWrapper(
                hotel = hotelDetails,
                isDarkTheme = isDark,
                isDescriptionExpanded = hotelDetailState.isDescriptionExpanded,
                showAllAmenities = hotelDetailState.showAmenitiesDialog,
                showFullMap = hotelDetailState.showFullMap,
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
                onDismissAmenitiesOverlay = {
                    viewModel.onEvent(HotelDetailsEvent.DismissAmenities)
                },
                onMapClick = {
                    viewModel.onEvent(HotelDetailsEvent.ViewFullMap)
                },
                onDismissMap = {
                    viewModel.onEvent(HotelDetailsEvent.DismissMap)
                },
                onSeeMoreNearbyClick = {
                    viewModel.onEvent(HotelDetailsEvent.ViewExploreArea)
                },
                showExploreArea = hotelDetailState.showExploreArea,
                areaDescription = hotelDetailState.areaDescription,
                nearbyTransport = hotelDetailState.nearbyTransport,
                nearbyRestaurants = hotelDetailState.nearbyRestaurants,
                isPoiLoading = hotelDetailState.isPoiLoading,
                poiErrorMessage = hotelDetailState.poiErrorMessage,
                onRetryPois = {
                    viewModel.onEvent(HotelDetailsEvent.RetryPois)
                },
                onDismissExploreArea = {
                    viewModel.onEvent(HotelDetailsEvent.DismissExploreArea)
                },
                modifier = modifier
            )
        }
    }
}

/**
 * A detail wrapper for a hotel or travel destination.
 * Displays information such as image carousel, description, amenities, a map and a gallery.
 *
 * @param hotel The data model containing all the information to be displayed.
 * @param modifier The modifier to be applied to the screen.
 * @param isDarkTheme Whether the theme is currently in dark mode.
 * @param isDescriptionExpanded Whether the description section is currently expanded.
 * @param showAllAmenities Whether the amenities overlay is currently visible.
 * @param showFullMap Whether the full-screen map overlay is currently visible.
 * @param onBackClick The action to perform when the back button is clicked.
 * @param onSeeAllPhotosClick The action to perform when the "See all photos" button is clicked.
 * @param onShareClick The action to perform when the share button is clicked.
 * @param onFavoriteClick The action to perform when the favorite button is clicked.
 * @param onBookClick The action to perform when the "Book Now" button is clicked.
 * @param onDescriptionExpandClick The action to perform when the description expand button is clicked.
 * @param onSeeAllAmenitiesClick The action to perform when the "See all amenities" button is clicked.
 * @param onDismissAmenitiesOverlay The action to perform when the amenities overlay is dismissed.
 * @param onMapClick The action to perform when the map preview is clicked.
 * @param onDismissMap The action to perform when the full-screen map is dismissed.
 * @param onSeeMoreNearbyClick The action to perform when the "See more" button in nearby places is clicked.
 * @param showExploreArea Whether the explore area overlay is currently visible.
 * @param areaDescription A description of the area.
 * @param onDismissExploreArea The action to perform when the explore area overlay is dismissed.
 */
@Composable
private fun TravelHotelDetailsWrapper(
    hotel: Hotel,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    isDescriptionExpanded: Boolean = false,
    showAllAmenities: Boolean = false,
    showFullMap: Boolean = false,
    onBackClick: () -> Unit = {},
    onSeeAllPhotosClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onBookClick: () -> Unit = {},
    onDescriptionExpandClick: () -> Unit = {},
    onSeeAllAmenitiesClick: () -> Unit = {},
    onDismissAmenitiesOverlay: () -> Unit = {},
    onMapClick: () -> Unit = {},
    onDismissMap: () -> Unit = {},
    onSeeMoreNearbyClick: () -> Unit = {},
    showExploreArea: Boolean = false,
    areaDescription: String? = null,
    nearbyTransport: List<Poi> = emptyList(),
    nearbyRestaurants: List<Poi> = emptyList(),
    isPoiLoading: Boolean = false,
    poiErrorMessage: String? = null,
    onRetryPois: () -> Unit = {},
    onDismissExploreArea: () -> Unit = {},
) {
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                Surface(shadowElevation = TravelinDimens.ElevationLarge) {
                    TravelDetailsBottomBar(
                        price = "$${formatPrice(hotel.pricePerNight)}",
                        onBookClick = onBookClick
                    )
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                item {
                    TravelDetailsHeader(
                        imageList = hotel.imageList,
                        name = hotel.name,
                        rating = hotel.reviewRating,
                        limitedReviews = hotel.limitedReviews,
                        onBackClick = onBackClick,
                        onSeeAllPhotosClick = onSeeAllPhotosClick,
                        onShareClick = onShareClick,
                        onFavoriteClick = onFavoriteClick
                    )
                }
                item {
                    TravelDetailsDescription(
                        description = hotel.description,
                        isExpanded = isDescriptionExpanded,
                        onExpandClick = onDescriptionExpandClick
                    )
                }
                item {
                    TravelDetailsFeaturesSection(
                        features = hotel.amenities,
                        title = stringResource(id = R.string.about_this_property_label),
                        seeAllLabel = stringResource(id = R.string.see_all_about_property),
                        onSeeAllClick = onSeeAllAmenitiesClick
                    )
                }
                item {
                    TravelDetailsMap(
                        address = hotel.address,
                        latitude = hotel.latitude,
                        longitude = hotel.longitude,
                        isDarkTheme = isDarkTheme,
                        onMapClick = onMapClick
                    )
                    Divider()
                }
                item {
                    NearbyPlacesSection(
                        nearbyPlaces = hotel.nearbyPlaces,
                        isLoading = isPoiLoading,
                        errorMessage = poiErrorMessage,
                        onRetry = onRetryPois,
                        onSeeMoreClick = onSeeMoreNearbyClick
                    )
                    Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))
                }
            }
        }

        if (showFullMap) {
            MapOverlay(
                hotelCoordinates = LatLng(hotel.latitude, hotel.longitude),
                isDarkTheme = isDarkTheme,
                onDismiss = onDismissMap
            )
        }

        if (showAllAmenities) {
            FeaturesOverlay(
                features = hotel.amenities,
                title = stringResource(id = R.string.about_this_property_label),
                onDismiss = onDismissAmenitiesOverlay
            )
        }

        if (showExploreArea) {
            ExploreAreaOverlay(
                nearbyPlaces = hotel.nearbyPlaces,
                areaDescription = areaDescription,
                nearbyTransport = nearbyTransport,
                nearbyRestaurants = nearbyRestaurants,
                isLoading = isPoiLoading,
                errorMessage = poiErrorMessage,
                onRetry = onRetryPois,
                onDismiss = onDismissExploreArea
            )
        }
    }
}
@Composable
private fun Divider(){
    HorizontalDivider(
        modifier = Modifier
            .padding(
                vertical = TravelinDimens.PaddingLarge,
                horizontal = TravelinDimens.PaddingLarge
            )
    )
}

@Preview(showBackground = true)
@Composable
private fun TravelHotelDetailsWrapperPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        TravelHotelDetailsWrapper(
            hotel = Hotel(
                id = "1",
                imageList = listOf(
                    "https://picsum.photos/200",
                    "https://picsum.photos/id/1020/800/600",
                    "https://images.unsplash.com/photo-1582719508461-905c673771fd",
                    "https://images.unsplash.com/photo-1582719508461-905c673771fd",
                    "https://images.unsplash.com/photo-1582719508461-905c673771fd",
                    "https://images.unsplash.com/photo-1582719508461-905c673771fd",
                    "https://images.unsplash.com/photo-1582719508461-905c673771fd"
                ),
                name = "Koh Rong Samloem",
                numberOfReviews = 30,
                reviewRating = 3.6,
                description = LoremIpsum(words = 50).values.first(),
                amenities = listOf(
                    "hotel.fitness_center",
                    "hotel.pool",
                    "hotel.breakfast",
                    "hotel.ac",
                    "hotel.wifi",
                    "hotel.cleaning_services",
                    "hotel.room_service"
                ),
                address = "Jalan Sunset Road No. 101, Kuta, Bali",
                latitude = 1.35,
                longitude = 103.87
            ),
            isDarkTheme = false,
            isPoiLoading = false,
            poiErrorMessage = null,
            onRetryPois = {}
        )
    }
}
