package com.softserveacademy.home.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.softserveacademy.core.presentation.design_system.theme.*
import com.softserveacademy.home.domain.repository.SearchFilter
import com.softserveacademy.home.domain.repository.SearchItem
import com.softserveacademy.home.presentation.viewmodel.SearchUiState
import com.softserveacademy.home.presentation.viewmodel.SearchViewModel

/**
 * Main screen for the Destination Search feature.
 * Provides a search bar, filtering options, and displays search results for hotels and tours.
 *
 * @param onBackClick Callback for handling navigation back.
 * @param onItemClick Callback when a search result item is clicked.
 * @param viewModel The [SearchViewModel] that manages the search logic and state.
 */
@Composable
fun DestinationSearchScreen(
    onBackClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
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
                onFilterSelected = { viewModel.onFilterChanged(it) }
            )

            // Handle UI states based on the ViewModel's state
            when (val state = viewModel.uiState) {
                is SearchUiState.Loading -> LoadingState()
                is SearchUiState.Idle, is SearchUiState.Success -> {
                    val items = (state as? SearchUiState.Success)?.items ?: emptyList()
                    // Display the nearby header only if the search query is currently empty
                    if (viewModel.searchQuery.isEmpty() && items.isNotEmpty()) {
                        NearbyHeader()
                    }
                    ResultsList(items, onItemClick)
                }

                is SearchUiState.Empty -> EmptyState()
                is SearchUiState.Error -> ErrorState(state.message) { viewModel.performSearch() }
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
            FilterChip(
                label = filter.name.lowercase().replaceFirstChar { it.uppercase() },
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
fun NearbyHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(TravelinDimens.PaddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(TravelinDimens.IconSizeExtraLarge)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = LocationMarkerIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(Modifier.width(TravelinDimens.SpaceMedium))
        Column {
            Text(
                text = "Search place nearby",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Current location - Santiago",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Displays search results in a vertical list.
 */
@Composable
fun ResultsList(items: List<SearchItem>, onItemClick: (Int) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(TravelinDimens.PaddingMedium),
        verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
    ) {
        items(items) { item ->
            when (item) {
                is SearchItem.HotelItem -> SearchResultCard(
                    title = item.hotel.name,
                    location = item.hotel.address,
                    image = item.hotel.image.firstOrNull(),
                    price = "${item.hotel.pricePerNight}",
                    rating = item.hotel.userRating,
                    onClick = { item.hotel.id?.let { onItemClick(it) } }
                )

                is SearchItem.TourItem -> SearchResultCard(
                    title = item.tour.title,
                    location = item.tour.location,
                    image = item.tour.imageUrl,
                    price = "${item.tour.price}",
                    rating = item.tour.rating.toDouble()
                    // Handle tour selection if needed
                )
                // Handle additional search result types as needed
                else -> {}
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
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier
                    .size(TravelinDimens.ImageSizeMedium)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = StarIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(TravelinDimens.IconSizeSmall)
                    )
                    Text(
                        text = " $rating",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "from $$price/person",
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
    SearchFilter.HOTELS -> HotelIcon
    SearchFilter.FLIGHTS -> PlaneIcon
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