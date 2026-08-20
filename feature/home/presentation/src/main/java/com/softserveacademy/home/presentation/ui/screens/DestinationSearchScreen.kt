package com.softserveacademy.home.presentation.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.softserveacademy.core.presentation.design_system.theme.*
import androidx.compose.ui.res.painterResource
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.softserveacademy.core.presentation.design_system.R as DesignR
import com.softserveacademy.home.domain.repository.SearchFilter
import com.softserveacademy.home.domain.repository.SearchItem
import com.softserveacademy.home.presentation.model.TravelItemType
import com.softserveacademy.home.presentation.viewmodel.SearchUiState
import com.softserveacademy.home.presentation.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * Main screen for the Destination Search feature.
 * Provides a search bar, filtering options, and displays search results for hotels and tours.
 *
 * @param onBackClick Callback for handling navigation back.
 * @param onItemClick Callback when a search result item is clicked.
 * @param viewModel The [SearchViewModel] that manages the search logic and state.
 */
@SuppressLint("MissingPermission")
@Composable
fun DestinationSearchScreen(
    onBackClick: () -> Unit,
    onItemClick: (String, TravelItemType) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var currentCity by remember { mutableStateOf("Location not found") }
    val scope = rememberCoroutineScope()

    val updateCityName: (Double, Double) -> Unit = { lat, lon ->
        scope.launch(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                val cityName = addresses?.firstOrNull()?.locality
                    ?: addresses?.firstOrNull()?.subAdminArea
                    ?: addresses?.firstOrNull()?.adminArea

                if (cityName != null) {
                    withContext(Dispatchers.Main) {
                        currentCity = cityName
                    }
                }
            } catch (e: Exception) {
                // Network or Geocoder service unavailable
            }
        }
    }

    // Attempt to get location and city name on start if permission is already granted
    LaunchedEffect(Unit) {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    updateCityName(location.latitude, location.longitude)
                    viewModel.currentLatitude = location.latitude
                    viewModel.currentLongitude = location.longitude
                }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            updateCityName(location.latitude, location.longitude)
                            viewModel.toggleNearbyMode(true, location.latitude, location.longitude)
                        } else {
                            // Fallback or mock if location is null
                            viewModel.toggleNearbyMode(true, -33.4489, -70.6693)
                        }
                    }
            } catch (e: SecurityException) {
                // Should not happen as we just got permission
            }
        } else {
            viewModel.onPermissionDenied()
        }
    }

    Scaffold(
        topBar = {
            SearchTopBar(
                query = viewModel.searchQuery,
                onQueryChange = { viewModel.onQueryChanged(it) },
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            FilterRow(
                selectedFilter = viewModel.currentFilter,
                onFilterSelected = { filter ->
                    if (filter == SearchFilter.POIS) {
                        val permissionCheck = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        } else {
                            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                                .addOnSuccessListener { location ->
                                    if (location != null) {
                                        viewModel.currentLatitude = location.latitude
                                        viewModel.currentLongitude = location.longitude
                                    }
                                    viewModel.onFilterChanged(filter)
                                }
                        }
                    } else {
                        viewModel.onFilterChanged(filter)
                    }
                }
            )

            if (viewModel.isNearbyMode || viewModel.currentFilter == SearchFilter.POIS) {
                RadiusSlider(
                    radius = viewModel.searchRadius,
                    onRadiusChange = { viewModel.onRadiusChanged(it) }
                )
            }

            // Handle UI states based on the ViewModel's state
            when (val state = viewModel.uiState) {
                is SearchUiState.Loading -> LoadingState()
                is SearchUiState.Idle, is SearchUiState.Success -> {
                    val items = (state as? SearchUiState.Success)?.items ?: emptyList()
                    // Display the nearby header only if the search query is currently empty
                    if (viewModel.searchQuery.isEmpty()) {
                        NearbyHeader(
                            onClick = {
                                val permissionCheck = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                )
                                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                        if (location != null) {
                                            updateCityName(location.latitude, location.longitude)
                                            viewModel.toggleNearbyMode(
                                                !viewModel.isNearbyMode,
                                                location.latitude,
                                                location.longitude
                                            )
                                        } else {
                                            viewModel.toggleNearbyMode(!viewModel.isNearbyMode, -33.4489, -70.6693)
                                        }
                                    }
                                } else {
                                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                }
                            },
                            isNearbyMode = viewModel.isNearbyMode,
                            locationName = currentCity
                        )
                    }
                    ResultsList(
                        items = items,
                        onItemClick = onItemClick
                    )
                }

                is SearchUiState.Empty -> EmptyState()
                is SearchUiState.Error -> ErrorState(state.message) { viewModel.performSearch() }
                is SearchUiState.PermissionDenied -> PermissionErrorState {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }
}

/**
 * Top bar component containing the search input field and back navigation button.
 *
 * @param query The current text in the search field.
 * @param onQueryChange Callback invoked when the search text changes.
 * @param onBackClick Callback for the back button.
 */
@Composable
fun SearchTopBar(query: String, onQueryChange: (String) -> Unit, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(TravelinDimens.PaddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = ArrowLeftIcon,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraLarge),
            placeholder = {
                Text(
                    text = "Where do you plan to go?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = SearchIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            shape = MaterialTheme.shapes.extraLarge
        )
    }
}

/**
 * Displays a list of available search filters in a horizontal row.
 *
 * @param selectedFilter The currently active filter.
 * @param onFilterSelected Callback when a filter is selected.
 */
@Composable
fun FilterRow(selectedFilter: SearchFilter, onFilterSelected: (SearchFilter) -> Unit) {
    LazyRow(
        modifier = Modifier.padding(
            horizontal = TravelinDimens.PaddingMedium,
            vertical = TravelinDimens.PaddingSmall
        ),
        horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
    ) {
        items(SearchFilter.entries) { filter ->
            val isSelected = selectedFilter == filter
            val label = if (filter == SearchFilter.POIS) "POIs" else filter.name.lowercase().replaceFirstChar { it.uppercase() }
            FilterChip(
                label = label,
                icon = getFilterIcon(filter),
                isSelected = isSelected,
                onClick = { onFilterSelected(filter) }
            )
        }
    }
}

/**
 * A selectable chip representing a single search filter.
 */
@Composable
fun FilterChip(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = MaterialTheme.shapes.large,
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = TravelinDimens.PaddingNormal,
                vertical = TravelinDimens.PaddingSmall
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(TravelinDimens.IconSizeSmall),
                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(TravelinDimens.SpaceExtraSmall))
            Text(
                text = label,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

/**
 * Header section showing the user's current location and prompt for nearby searches.
 */
@Composable
fun NearbyHeader(onClick: () -> Unit, isNearbyMode: Boolean, locationName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(TravelinDimens.PaddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(TravelinDimens.IconSizeExtraLarge)
                .background(
                    if (isNearbyMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = LocationMarkerIcon,
                contentDescription = null,
                tint = if (isNearbyMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
            )
        }
        Spacer(Modifier.width(TravelinDimens.SpaceMedium))
        Column {
            Text(
                text = "Search nearby",
                style = MaterialTheme.typography.titleMedium,
                color = if (isNearbyMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Current location - $locationName",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun RadiusSlider(radius: Float, onRadiusChange: (Float) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = TravelinDimens.PaddingMedium)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Distance radius",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${radius.toInt()} km",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Slider(
            value = radius,
            onValueChange = onRadiusChange,
            valueRange = 1f..100f,
            steps = 99,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}

@Composable
fun ResultsList(
    items: List<SearchItem>,
    onItemClick: (String, TravelItemType) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(TravelinDimens.PaddingMedium),
        verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
    ) {
        items(items) { item ->
            // ... (rest of items)
            when (item) {
                is SearchItem.HotelItem -> SearchResultCard(
                    title = item.hotel.name,
                    location = item.hotel.address,
                    image = item.hotel.imageList.firstOrNull(),
                    price = "$\$item.hotel.pricePerNight",
                    rating = item.hotel.reviewRating,
                    ratingText = item.hotel.limitedReviews,
                    onClick = { onItemClick(item.hotel.id, TravelItemType.HOTEL) }
                )

                is SearchItem.TourItem -> SearchResultCard(
                    title = item.tour.title,
                    location = item.tour.location,
                    image = item.tour.imageList.firstOrNull(),
                    price = "${item.tour.rates}",
                    rating = item.tour.rating,
                    ratingText = item.tour.limitedReviews,
                    onClick = { onItemClick(item.tour.id, TravelItemType.TOUR) }
                )

                is SearchItem.DestinationItem -> SearchResultCard(
                    title = item.destination.name,
                    location = item.destination.location,
                    image = item.destination.imageUrl,
                    price = "${item.destination.pricePerPax}",
                    rating = item.destination.rating,
                    onClick = { onItemClick(item.destination.id, TravelItemType.DESTINATION) }
                )

                is SearchItem.PoiItem -> SearchResultCard(
                    title = item.poi.name,
                    location = "${item.poi.type} • ${item.poi.travelTime}",
                    image = item.poi.imageUrl,
                    price = item.poi.distanceMeters?.let { "%.1f km".format(it / 1000.0) } ?: "",
                    priceLabel = "Distance:",
                    rating = null,
                    onClick = {
                        onItemClick(
                            "${item.poi.name}|${item.poi.latitude}|${item.poi.longitude}",
                            TravelItemType.POI
                        )
                    }
                )
            }
        }
    }
}

/**
 * A reusable card component to display search result details.
 *
 * @param title The name or title of the result.
 * @param location The address or location description.
 * @param image The URL or resource path for the image.
 * @param price The formatted price string.
 * @param rating The numeric rating.
 * @param onClick Callback when the card is clicked.
 */
@Composable
fun SearchResultCard(
    title: String,
    location: String,
    image: String?,
    price: String,
    rating: Double?,
    ratingText: String? = null,
    priceLabel: String = "from $",
    onClick: () -> Unit = {}
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shadowElevation = TravelinDimens.ElevationSmall
    ) {
        Row(
            modifier = Modifier
                .padding(TravelinDimens.PaddingSmall)
                .fillMaxWidth()
                .height(TravelinDimens.ImageSizeMedium)
        ) {
            Box(
                modifier = Modifier
                    .size(TravelinDimens.ImageSizeMedium)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (image != null) {
                    AsyncImage(
                        model = image,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = painterResource(DesignR.drawable.ic_bag)
                    )
                } else {
                    Icon(
                        imageVector = Bag,
                        contentDescription = null,
                        modifier = Modifier.size(TravelinDimens.IconSizeLarge),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(start = TravelinDimens.PaddingNormal)
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = location,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (rating != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = StarIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(TravelinDimens.IconSizeSmall)
                        )
                        Text(
                            text = buildString {
                                append(" $rating")
                                if (!ratingText.isNullOrEmpty()) {
                                    append(" ($ratingText)")
                                }
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Text(
                    text = if (priceLabel == "Distance:") "$priceLabel $price" else "$priceLabel$price/person",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

/**
 * Utility function to get the appropriate icon for a given [SearchFilter].
 */
@Composable
fun getFilterIcon(filter: SearchFilter) = when (filter) {
    SearchFilter.ALL -> SearchIcon
    SearchFilter.POIS -> StarIcon
    SearchFilter.HOTELS -> HotelIcon
    SearchFilter.TOURS -> TicketIcon
    SearchFilter.DESTINATIONS -> LocationMarkerIcon

}

// --- UI States components ---

/**
 * Centered loading indicator for the search screen.
 */
@Composable
fun LoadingState() {
    Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
}

/**
 * Screen displayed when no search results are found.
 */
@Composable
fun EmptyState() {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Text(
            text = "No results found",
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

/**
 * Error state screen with a retry option.
 */
@Composable
fun ErrorState(msg: String, onRetry: () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Text(text = msg, color = MaterialTheme.colorScheme.error)
        Button(onClick = onRetry) { Text("Retry") }
    }
}

/**
 * Screen displayed when location permission is denied.
 */
@Composable
fun PermissionErrorState(onRetry: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(TravelinDimens.PaddingLarge),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = LocationMarkerIcon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(Modifier.height(TravelinDimens.SpaceMedium))
        Text(
            text = "Location permission is required to find nearby places.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(TravelinDimens.SpaceMedium))
        Button(onClick = onRetry) {
            Text("Grant Permission")
        }
    }
}
