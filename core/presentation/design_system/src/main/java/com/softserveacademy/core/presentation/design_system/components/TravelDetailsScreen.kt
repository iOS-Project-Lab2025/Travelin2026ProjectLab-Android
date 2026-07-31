package com.softserveacademy.core.presentation.design_system.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.softserveacademy.core.domain.model.DestinationDetails
import com.softserveacademy.core.domain.model.IncludedItems
import com.softserveacademy.core.presentation.design_system.R
import com.softserveacademy.core.presentation.design_system.components.util.detailsScreenUtilities.TravelBookingBar
import com.softserveacademy.core.presentation.design_system.theme.AngleLeftIcon
import com.softserveacademy.core.presentation.design_system.theme.AngleRightIcon
import com.softserveacademy.core.presentation.design_system.theme.BlueDark90_Alpha50
import com.softserveacademy.core.presentation.design_system.theme.LocationMarkerIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.core.presentation.design_system.components.util.detailsScreenUtilities.TravelHotelDetailsTopIcons
import com.softserveacademy.core.presentation.design_system.components.util.detailsScreenUtilities.TravelIncludedItem
import com.softserveacademy.core.presentation.design_system.components.util.reusable_icons.TravelArrowIcon

/**
 * A detail screen for a hotel or travel destination.
 * Displays information such as image carousel, description, what's included, and a gallery.
 *
 * @param destinationDetails The data model containing all the information to be displayed.
 * @param modifier The modifier to be applied to the screen.
 * @param onBackClick The action to perform when the back button is clicked.
 * @param onShareClick The action to perform when the share button is clicked.
 * @param onFavoriteClick The action to perform when the favorite button is clicked.
 * @param onBookClick The action to perform when the "Book Now" button is clicked.
 */
@Composable
fun TravelDetailsScreen(
    destinationDetails: DestinationDetails,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    isDescriptionExpanded: Boolean = false,
    showAmenitiesDialog: Boolean = false,
    showFullMap: Boolean = false,
    onBackClick: () -> Unit = {},
    onSeeAllPhotosClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onBookClick: () -> Unit = {},
    onDescriptionExpandClick: () -> Unit = {},
    onSeeAllAmenitiesClick: () -> Unit = {},
    onDismissAmenitiesDialog: () -> Unit = {},
    onMapClick: () -> Unit = {},
    onDismissMap: () -> Unit = {}
) {
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                Surface(shadowElevation = 8.dp) {
                    TravelBookingBar(
                        price = destinationDetails.minimumPrice,
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
                    HeaderImage(
                        imageList = destinationDetails.imageList,
                        name = destinationDetails.name,
                        rating = destinationDetails.rating,
                        limitedReviews = destinationDetails.limitedReviews,
                        limitedImages = destinationDetails.limitedImages,
                        onBackClick = onBackClick,
                        onSeeAllPhotosClick = onSeeAllPhotosClick,
                        onShareClick = onShareClick,
                        onFavoriteClick = onFavoriteClick
                    )
                }
                item {
                    DescriptionSection(
                        description = destinationDetails.description,
                        isExpanded = isDescriptionExpanded,
                        onExpandClick = onDescriptionExpandClick
                    )
                }
                item {
                    IncludingSection(
                        includedItems = destinationDetails.includedItems,
                        onSeeAllClick = onSeeAllAmenitiesClick
                    )
                }
                item {
                    LocationSection(
                        address = destinationDetails.address,
                        latitude = destinationDetails.latitude,
                        longitude = destinationDetails.longitude,
                        isDarkTheme = isDarkTheme,
                        onMapClick = onMapClick
                    )
                }
                item {
                    GallerySection(
                        imageList = destinationDetails.imageList,
                        numberOfImages = destinationDetails.imageList.size,
                        onSeeAllPhotosClick = onSeeAllPhotosClick
                    )
                }
            }
        }

        if (showFullMap) {
            MapOverlay(
                hotelCoordinates = LatLng(destinationDetails.latitude, destinationDetails.longitude),
                isDarkTheme = isDarkTheme,
                onDismiss = onDismissMap
            )
        }

        if (showAmenitiesDialog) {
            AmenitiesOverlay(
                includedItems = destinationDetails.includedItems,
                onDismiss = onDismissAmenitiesDialog
            )
        }
    }
}

/**
 * Displays the header section of the detail screen, including the image carousel
 * and basic info.
 *
 * @param imageList List of image URLs to show in the carousel.
 * @param name The name of the destination.
 * @param rating The destination's user rating.
 * @param limitedReviews Formatted string representing the number of reviews.
 * @param limitedImages Formatted string representing the number of images.
 * @param onBackClick Action to perform when the back button is clicked.
 * @param onSeeAllPhotosClick Action to perform when the images button is clicked.
 * @param onShareClick Action to perform when the share button is clicked.
 * @param onFavoriteClick Action to perform when the favorite button is clicked.
 */
@Composable
private fun HeaderImage(
    imageList : List<String>,
    name : String,
    rating : Double,
    limitedReviews : String,
    limitedImages : String,
    onBackClick: () -> Unit,
    onSeeAllPhotosClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {

        TravelGalleryCarousel(
            images = imageList.take(5),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        TravelHotelDetailsTopIcons(onBackClick,onShareClick,onFavoriteClick)

        Column(
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    start = TravelinDimens.PaddingMedium,
                    end = TravelinDimens.PaddingMedium,
                    bottom = TravelinDimens.Padding2ExtraLarge
                )
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {

                TravelRatingBar(rating = rating)

                Spacer(modifier = Modifier.width(TravelinDimens.SpaceSmall))

                Text(
                    text = limitedReviews,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    start = TravelinDimens.PaddingMedium,
                    end = TravelinDimens.PaddingMedium,
                    bottom = TravelinDimens.Padding2ExtraLarge
                )
                .clickable { onSeeAllPhotosClick() },
            color = BlueDark90_Alpha50,
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = limitedImages,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(
                        horizontal = TravelinDimens.PaddingSmall,
                        vertical = TravelinDimens.PaddingExtraSmall
                    ),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }

    }
}

/**
 * Displays the description section of the destination, with "Read more/less" functionality.
 *
 * @param description The full description text of the destination.
 * @param isExpanded Whether the description is currently expanded.
 * @param onExpandClick Callback when the expand/collapse button is clicked.
 */
@Composable
private fun DescriptionSection(
    description: String,
    isExpanded: Boolean,
    onExpandClick: () -> Unit
) {
    var hasOverflow by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(TravelinDimens.PaddingLarge)
    ) {
        Text(
            text = stringResource(id = R.string.about_label),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onPrimaryFixed,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

        Column {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = if (isExpanded) Int.MAX_VALUE else 6,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult ->
                    if (!isExpanded) {
                        hasOverflow = textLayoutResult.hasVisualOverflow
                    }
                }
            )
            if (hasOverflow || isExpanded) {
                Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))
                Row(
                    modifier = Modifier
                        .clickable { onExpandClick() }
                        .padding(vertical = TravelinDimens.PaddingSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isExpanded) stringResource(id = R.string.read_less_label)
                        else stringResource(id = R.string.read_more_label),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(TravelinDimens.SpaceExtraSmall))
                    Icon(
                        imageVector = if (isExpanded) AngleLeftIcon else AngleRightIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(TravelinDimens.IconSizeExtraSmall)
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(
                    vertical = TravelinDimens.PaddingLarge
                )
        )
    }
}

/**
 * Displays the "What's included" section, showing amenities in a two-column grid.
 *
 * @param includedItems List of UI models representing the amenities.
 * @param onSeeAllClick Callback when the "See all" button is clicked.
 */
@Composable
private fun IncludingSection(
    includedItems: List<IncludedItems>,
    onSeeAllClick: () -> Unit
) {
    if (includedItems.isEmpty()) return

    val displayItems = includedItems.take(6)
    val hasMore = includedItems.size > 6

    Column(modifier = Modifier
        .padding(
            horizontal = TravelinDimens.PaddingLarge
        )
    ) {
        Text(
            text = stringResource(id = R.string.What_is_included_label),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimaryFixed,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        displayItems.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
            ) {
                rowItems.forEach { item ->
                    TravelIncludedItem(
                        item = item,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))
        }

        if (hasMore) {
            Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))
            TravelOutlinedButton(
                text = stringResource(id = R.string.see_all_about_property),
                onClick = onSeeAllClick,
                contentPadding = PaddingValues(
                    horizontal = TravelinDimens.PaddingLarge,
                    vertical = TravelinDimens.PaddingSmall
                )
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(
                    top = TravelinDimens.PaddingLarge,
                    bottom = TravelinDimens.PaddingExtraLarge
                )
        )
    }
}

/**
 * Full-screen overlay displaying all amenities included in the hotel.
 *
 * @param includedItems Full list of amenities.
 * @param onDismiss Callback to close the overlay.
 */
@Composable
private fun AmenitiesOverlay(
    includedItems: List<IncludedItems>,
    onDismiss: () -> Unit
) {
    BackHandler(onBack = onDismiss)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = TravelinDimens.PaddingLarge)
                .padding(bottom = TravelinDimens.PaddingLarge)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TravelArrowIcon(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                Text(
                    text = stringResource(id = R.string.What_is_included_label),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium),
                modifier = Modifier.weight(1f)
            ) {
                items(includedItems.chunked(2)) { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
                    ) {
                        rowItems.forEach { item ->
                            TravelIncludedItem(
                                item = item,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

/**
 * Displays the location section of the hotel, including the address and a Google Map.
 * The map's theme automatically adjusts to match the system's dark or light mode.
 *
 * @param address The formatted address string of the hotel.
 * @param latitude The latitude coordinate of the hotel.
 * @param longitude The longitude coordinate of the hotel.
 * @param onMapClick Callback when the map preview is clicked.
 */
@Composable
private fun LocationSection(
    address : String,
    latitude : Double,
    longitude : Double,
    isDarkTheme: Boolean,
    onMapClick: () -> Unit
){
    val hotelCoordinates = LatLng(latitude, longitude)
    val context = LocalContext.current
    val mapProperties = remember(isDarkTheme) {
        MapProperties(
            mapStyleOptions = if (isDarkTheme) {
                MapStyleOptions.loadRawResourceStyle(
                    context,
                    R.raw.map_style_dark
                )
            } else {
                null
            }
        )
    }

    Column(modifier = Modifier
        .padding(
            horizontal = TravelinDimens.PaddingLarge
        )
    ) {
        Text(
            text = stringResource(id = R.string.Where_it_is_label),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimaryFixed,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        Text(
            text = address,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium)
                .shadow(TravelinDimens.ElevationSmall)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                    MaterialTheme.shapes.medium
                )
                .clickable { onMapClick() }
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(hotelCoordinates, 16f)
                },
                properties = mapProperties,
                uiSettings = MapUiSettings(
                    scrollGesturesEnabled = false,
                    zoomGesturesEnabled = false,
                    tiltGesturesEnabled = false,
                    rotationGesturesEnabled = false,
                    zoomControlsEnabled = false
                ),
                onMapClick = { onMapClick() }
            ) {
                MarkerComposable(
                    state = rememberUpdatedMarkerState(position = hotelCoordinates)
                ) {
                    Icon(
                        imageVector = LocationMarkerIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(TravelinDimens.IconSizeExtraLarge)
                    )
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .padding(
                    top = TravelinDimens.PaddingLarge,
                    bottom = TravelinDimens.PaddingExtraLarge
                )
        )
    }
}

/**
 * Displays a full-screen version of the map where the user can navigate and interact.
 *
 * @param hotelCoordinates The coordinates to center the map on.
 * @param isDarkTheme Whether the map should use the dark theme.
 * @param onDismiss Action to perform to close the full-screen view.
 */
@Composable
private fun MapOverlay(
    hotelCoordinates: LatLng,
    isDarkTheme: Boolean,
    onDismiss: () -> Unit
) {
    // BackHandler is active only when the overlay is visible
    BackHandler(onBack = onDismiss)

    val context = LocalContext.current
    val mapProperties = remember(isDarkTheme) {
        MapProperties(
            mapStyleOptions = if (isDarkTheme) {
                MapStyleOptions.loadRawResourceStyle(
                    context,
                    R.raw.map_style_dark
                )
            } else {
                null
            }
        )
    }

    // Camera position state that persists across rotation
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(hotelCoordinates, 16f)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f),
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    compassEnabled = true
                ),
                contentPadding = PaddingValues(
                    top = TravelinDimens.PaddingMedium,
                    bottom = TravelinDimens.Padding2ExtraLarge
                )
            ) {
                MarkerComposable(
                    state = rememberUpdatedMarkerState(position = hotelCoordinates)
                ) {
                    Icon(
                        imageVector = LocationMarkerIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(TravelinDimens.IconSizeExtraLarge)
                    )
                }
            }

            TravelArrowIcon(
                modifier = Modifier
                    .padding(TravelinDimens.PaddingMedium),
                onClick = onDismiss
            )
        }
    }
}

/**
 * Displays a preview of the hotel gallery with 3 images and a button to view all photos.
 *
 * @param imageList List of image URLs to choose from.
 * @param numberOfImages Total number of images available.
 * @param onSeeAllPhotosClick Action to perform when the button is clicked.
 */
@Composable
private fun GallerySection(
    imageList: List<String>,
    numberOfImages: Int,
    onSeeAllPhotosClick: () -> Unit
) {
    if (imageList.isEmpty()) return

    Column(
        modifier = Modifier
            .padding(horizontal = TravelinDimens.PaddingLarge)
            .padding(bottom = TravelinDimens.PaddingLarge)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
            ) {
                if (imageList.isNotEmpty()) {
                    AsyncImage(
                        model = imageList[0],
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.test_hotel),
                        error = painterResource(R.drawable.test_hotel)
                    )
                }
                if (imageList.size > 1) {
                    AsyncImage(
                        model = imageList[1],
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.test_hotel),
                        error = painterResource(R.drawable.test_hotel)
                    )
                }
            }

            if (imageList.size > 2) {
                AsyncImage(
                    model = imageList[2],
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.test_hotel),
                    error = painterResource(R.drawable.test_hotel)
                )
            }
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

        TravelOutlinedButton(
            text = "${stringResource(id = R.string.see_all_label)} ${numberOfImages - 1} " +
                    stringResource(id = R.string.plus_photos_label),
            onClick = onSeeAllPhotosClick,
            contentPadding = PaddingValues(
                horizontal = TravelinDimens.PaddingLarge,
                vertical = TravelinDimens.PaddingSmall
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TravelDetailsScreenPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        TravelDetailsScreen(
            destinationDetails = DestinationDetails(
                id = 1,
                minimumPrice = 400,
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
                rating = 3.6,
                description = LoremIpsum(words = 50).values.first(),
                includedItems = listOf(
                    IncludedItems.FitnessCenter,
                    IncludedItems.Pool,
                    IncludedItems.BuffetBreakfast,
                    IncludedItems.AcUnit,
                    IncludedItems.FreeWifi,
                    IncludedItems.CleaningServices,
                    IncludedItems.RoomService
                ),
                address = "Jalan Sunset Road No. 101, Kuta, Bali",
                latitude = 1.35,
                longitude = 103.87
            ),
            isDarkTheme = false
        )
    }
}
