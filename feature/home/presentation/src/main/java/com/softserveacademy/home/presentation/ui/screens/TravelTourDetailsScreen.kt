package com.softserveacademy.home.presentation.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.model.LatLng
import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.domain.model.TourCategory
import com.softserveacademy.core.presentation.design_system.components.HotelDetailLoading
import com.softserveacademy.core.presentation.design_system.components.TravelErrorScreen
import com.softserveacademy.core.presentation.design_system.theme.LocalIsDarkTheme
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.home.presentation.R
import com.softserveacademy.home.presentation.events.TourDetailsEvent
import com.softserveacademy.home.presentation.events.TourDetailsEventEffect
import com.softserveacademy.home.presentation.ui.components.detailsScreenComponents.TravelDetailsDescription
import com.softserveacademy.home.presentation.ui.components.detailsScreenComponents.TravelDetailsMap
import com.softserveacademy.home.presentation.ui.components.detailsScreenComponents.MapOverlay
import com.softserveacademy.home.presentation.ui.components.detailsScreenComponents.TravelDetailsHeader
import com.softserveacademy.home.presentation.ui.components.detailsScreenComponents.TravelDetailsFeaturesSection
import com.softserveacademy.home.presentation.ui.components.detailsScreenComponents.FeaturesOverlay
import com.softserveacademy.home.presentation.viewmodel.TourDetailsViewModel
import kotlin.time.Duration


/**
 * Stateful screen that use the [TravelTourDetailsWrapper].
 *
 * This composable handles the connection between the UI and the [TourDetailsViewModel].
 * It collects the state from the ViewModel and displays the appropriate UI.
 *
 * @param itemId The unique identifier of the hotel or tour.
 * @param onBackClick Action to perform when the back button is clicked.
 * @param onSeeAllPhotosClick Action to perform when the "See all photos" button is clicked.
 * @param modifier The modifier to be applied to the layout.
 * @param viewModel The ViewModel that provides the hotel detail data.
 */
@Composable
fun TravelTourDetailsScreen(
    itemId: String,
    onBackClick: () -> Unit,
    onSeeAllPhotosClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TourDetailsViewModel = hiltViewModel(),
){
    val tourDetailState by viewModel.tourDetailsState.collectAsState()
    val context = LocalContext.current
    val isDark = LocalIsDarkTheme.current
    val tourDetails = tourDetailState.tourDetails
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(itemId){
        viewModel.onEvent(TourDetailsEvent.Load(itemId))
    }

    val shareTitle = stringResource(id = R.string.share_tour_title)
    val shareMessageTemplate = stringResource(id = R.string.share_tour_message)

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    TourDetailsEventEffect.NavigateBack -> onBackClick()
                    is TourDetailsEventEffect.NavigateToGallery -> onSeeAllPhotosClick()
                    is TourDetailsEventEffect.ShareTour -> {
                        val shareMessage = shareMessageTemplate.format(
                            effect.tour.title,
                            effect.tour.id
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
        tourDetailState.isLoading || (tourDetails == null && tourDetailState.errorMessage == null) -> {
            // TODO: Change loading screen depending of the final look of the tour.
            HotelDetailLoading()
        }
        tourDetailState.errorMessage != null -> {
            TravelErrorScreen(
                message = tourDetailState.errorMessage,
                onRetry = { viewModel.onEvent(TourDetailsEvent.Load(itemId)) }
            )
        }
        tourDetails != null -> {
            TravelTourDetailsWrapper(
                tour = tourDetails,
                isDarkTheme = isDark,
                isDescriptionExpanded = tourDetailState.isDescriptionExpanded,
                showFullMap = tourDetailState.showFullMap,
                showAllAmenities = tourDetailState.showAllAmenities,
                onBackClick = {
                    viewModel.onEvent(TourDetailsEvent.NavigateBack)
                },
                onSeeAllPhotosClick = {
                    viewModel.onEvent(TourDetailsEvent.ViewGallery)
                },
                onShareClick = {
                    viewModel.onEvent(TourDetailsEvent.Share)
                },
                onFavoriteClick = {
                    viewModel.onEvent(TourDetailsEvent.ToggleFavorite)
                },
                onDescriptionExpandClick = {
                    viewModel.onEvent(TourDetailsEvent.ToggleDescription)
                },
                onSeeAllAmenitiesClick = {
                    viewModel.onEvent(TourDetailsEvent.ViewAllAmenities)
                },
                onDismissAmenitiesOverlay = {
                    viewModel.onEvent(TourDetailsEvent.DismissAmenities)
                },
                onMapClick = {
                    viewModel.onEvent(TourDetailsEvent.ViewFullMap)
                },
                onDismissMap = {
                    viewModel.onEvent(TourDetailsEvent.DismissMap)
                },
                modifier = modifier
            )
        }
    }
}



/**
 * A detail wrapper for a tour.
 * Displays information such as image carousel, description, amenities, a map and a gallery.
 *
 * @param tour The data model containing all the information to be displayed.
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
 */
@Composable
private fun TravelTourDetailsWrapper(
    tour: Tour,
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
    onDismissMap: () -> Unit = {}
) {
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                item {
                    TravelDetailsHeader(
                        imageList = tour.imageList,
                        name = tour.title,
                        rating = tour.rating,
                        limitedReviews = tour.limitedReviews,
                        onBackClick = onBackClick,
                        onSeeAllPhotosClick = onSeeAllPhotosClick,
                        onShareClick = onShareClick,
                        onFavoriteClick = onFavoriteClick
                    )
                }
                item {
                    TravelDetailsDescription(
                        description = tour.description,
                        isExpanded = isDescriptionExpanded,
                        onExpandClick = onDescriptionExpandClick
                    )
                }

                item {
                    TravelDetailsFeaturesSection(
                        features = tour.includedServices,
                        onSeeAllClick = onSeeAllAmenitiesClick
                    )
                }

                item {
                    TravelDetailsMap(
                        address = tour.location,
                        latitude = tour.latitude,
                        longitude = tour.longitude,
                        isDarkTheme = isDarkTheme,
                        onMapClick = onMapClick
                    )
                }
            }
        }

        if (showFullMap) {
            MapOverlay(
                hotelCoordinates = LatLng(tour.latitude, tour.longitude),
                isDarkTheme = isDarkTheme,
                onDismiss = onDismissMap
            )
        }

        if (showAllAmenities) {
            FeaturesOverlay(
                features = tour.includedServices,
                onDismiss = onDismissAmenitiesOverlay
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TravelTourDetailsWrapperPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        TravelTourDetailsWrapper(
            tour = Tour(
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
                title = "Koh Rong Samloem",
                numberOfReviews = 30,
                rating = 3.6,
                description = LoremIpsum(words = 50).values.first(),
                location = "Jalan Sunset Road No. 101, Kuta, Bali",
                latitude = 1.35,
                longitude = 103.87,
                duration = Duration.ZERO,
                price = 0.0,
                category = TourCategory.GASTRONOMY,
                includedServices = listOf(
                    "TOUR_TRANSPORT",
                    "TOUR_GUIDE",
                    "TOUR_TICKET",
                    "TOUR_SAFETY_EQUIPMENT",
                    "TOUR_PROFESSIONAL_GUIDE",
                    "TOUR_AUDIO_GUIDE",
                    "TOUR_LAUNCH"
                )
            ),
            isDarkTheme = false
        )
    }
}