package com.softserveacademy.home.presentation.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import com.google.android.gms.maps.model.LatLng
import com.softserveacademy.core.domain.model.AiRecommendation
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
import com.softserveacademy.home.presentation.koog.AiraVoiceFAB
import com.softserveacademy.home.presentation.koog.AiraThinkingIndicator
import com.softserveacademy.home.presentation.koog.AiraFeedbackBubble
import com.softserveacademy.home.presentation.koog.VoiceRecognizerContract

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

    val onNavigateClick: (AiRecommendation) -> Unit = { recommendation ->
        val gmmIntentUri = Uri.parse("geo:0,0?q=${recommendation.latitude},${recommendation.longitude}(${recommendation.name})")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    }

    val voiceLauncher = rememberLauncherForActivityResult(VoiceRecognizerContract()) { query ->
        query?.let {
            viewModel.onEvent(HotelDetailsEvent.VoiceSearch(it))
        }
    }

    val multiplePermissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val audioGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: false
        val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        
        if (audioGranted) {
            // If they clicked the mic and then granted permission
            // we might want to launch voice here, but we need to know if that was the intent.
            // For now, just having them granted is good.
        }
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            voiceLauncher.launch(Unit)
        }
    }

    LaunchedEffect(Unit) {
        // Ask for permissions on start if they misclicked previously
        val permissionsToRequest = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.RECORD_AUDIO)
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        
        if (permissionsToRequest.isNotEmpty()) {
            multiplePermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    LaunchedEffect(itemId){
        viewModel.onEvent(HotelDetailsEvent.Load(itemId))
    }

    // Auto-hide feedback bubble after 5 seconds
    LaunchedEffect(hotelDetailState.lastVoiceQuery) {
        if (hotelDetailState.lastVoiceQuery != null) {
            delay(5000)
            viewModel.onEvent(HotelDetailsEvent.ClearVoiceQuery)
        }
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
                    is HotelDetailsEventEffect.ShowAiError -> {
                        // In a real app we could show a toast or snackbar
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
            Box(modifier = modifier.fillMaxSize()) {
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
                    aiRecommendations = hotelDetailState.aiRecommendations,
                    isAiLoading = hotelDetailState.isAiLoading,
                    lastVoiceQuery = hotelDetailState.lastVoiceQuery,
                    isPoiLoading = hotelDetailState.isPoiLoading,
                    poiErrorMessage = hotelDetailState.poiErrorMessage,
                    onRetryPois = {
                        viewModel.onEvent(HotelDetailsEvent.RetryPois)
                    },
                    onDismissExploreArea = {
                        viewModel.onEvent(HotelDetailsEvent.DismissExploreArea)
                    },
                    onRecommendationClick = { recommendation ->
                        viewModel.onEvent(HotelDetailsEvent.SelectRecommendation(recommendation))
                    },
                    onNavigateClick = onNavigateClick,
                    onVoiceClick = {
                        val permissionCheck = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        )
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            voiceLauncher.launch(Unit)
                        } else {
                            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    }
                )

                // Recommendation Card overlay
                hotelDetailState.selectedRecommendation?.let { recommendation ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 80.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        com.softserveacademy.home.presentation.koog.RecommendationCard(
                            recommendation = recommendation,
                            onNavigateClick = onNavigateClick,
                            onDismiss = {
                                viewModel.onEvent(HotelDetailsEvent.SelectRecommendation(null))
                            }
                        )
                    }
                }
            }
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
    aiRecommendations: List<AiRecommendation> = emptyList(),
    isAiLoading: Boolean = false,
    lastVoiceQuery: String? = null,
    isPoiLoading: Boolean = false,
    poiErrorMessage: String? = null,
    onRetryPois: () -> Unit = {},
    onDismissExploreArea: () -> Unit = {},
    onRecommendationClick: (AiRecommendation) -> Unit = {},
    onNavigateClick: (AiRecommendation) -> Unit = {},
    onVoiceClick: () -> Unit = {},
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
            },
            floatingActionButton = {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 70.dp) // Offset to not cover bottom bar too much
                ) {
                    if (isAiLoading) {
                        AiraThinkingIndicator()
                    }
                    AiraVoiceFAB(onClick = onVoiceClick)
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

                if (aiRecommendations.isNotEmpty() || isAiLoading) {
                    item {
                        Column(
                            modifier = Modifier.padding(vertical = TravelinDimens.PaddingMedium)
                        ) {
                            Text(
                                text = "Aira Suggestions",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onPrimaryFixed,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = TravelinDimens.PaddingLarge)
                            )

                            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

                            if (isAiLoading && aiRecommendations.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().height(100.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AiraThinkingIndicator()
                                }
                            }

                            LazyRow(
                                contentPadding = PaddingValues(horizontal = TravelinDimens.PaddingLarge),
                                horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
                            ) {
                                items(aiRecommendations) { recommendation ->
                                    com.softserveacademy.home.presentation.koog.RecommendationCard(
                                        recommendation = recommendation,
                                        onDismiss = { /* No-op in list */ },
                                        onNavigateClick = onNavigateClick,
                                        modifier = Modifier.width(300.dp)
                                    )
                                }
                            }
                        }
                        Divider()
                    }
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
                aiRecommendations = aiRecommendations,
                isAiLoading = isAiLoading,
                lastVoiceQuery = lastVoiceQuery,
                onRecommendationClick = onRecommendationClick,
                onNavigateClick = onNavigateClick,
                onVoiceClick = onVoiceClick,
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
