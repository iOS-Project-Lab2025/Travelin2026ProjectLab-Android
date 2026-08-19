package com.softserveacademy.feature.favorites.common.presentation.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.ui.res.stringResource
import com.softserveacademy.feature.favorites.common.presentation.R
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
import com.softserveacademy.core.presentation.design_system.components.TravelTextActionButton
import com.softserveacademy.core.presentation.design_system.theme.ArrowLeftIcon
import com.softserveacademy.core.presentation.design_system.theme.HotelIcon
import com.softserveacademy.core.presentation.design_system.theme.SearchIcon
import com.softserveacademy.core.presentation.design_system.theme.TicketIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.feature.favorites.common.domain.model.FavoriteItem
import com.softserveacademy.feature.favorites.common.presentation.events.FavoriteType
import com.softserveacademy.feature.favorites.common.presentation.events.TravelFavoritesEffect
import com.softserveacademy.feature.favorites.common.presentation.events.TravelFavoritesEvent
import com.softserveacademy.feature.favorites.common.presentation.states.TravelFavoritesState
import com.softserveacademy.feature.favorites.common.presentation.ui.components.FavoriteCategoryShortcut
import com.softserveacademy.feature.favorites.common.presentation.ui.components.FavoriteItemCard
import com.softserveacademy.feature.favorites.common.presentation.ui.components.TravelEmptyFavorites
import com.softserveacademy.feature.favorites.common.presentation.ui.components.TravelFavoritesCategoryShortcuts
import com.softserveacademy.feature.favorites.common.presentation.viewmodel.FavoritesViewModel

@Composable
fun RootFavoritesScreen(
    onNavigateToHotels: () -> Unit,
    onNavigateToTours: () -> Unit,
    onNavigateToDetail: (String, FavoriteType) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TravelFavoritesEffect.NavigateToHotels -> onNavigateToHotels()
                TravelFavoritesEffect.NavigateToTours -> onNavigateToTours()
                is TravelFavoritesEffect.NavigateToDetail -> onNavigateToDetail(effect.id, effect.type)
                TravelFavoritesEffect.NavigateBack -> onBackClick()
            }
        }
    }

    TravelFavoritesScreen(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun TravelFavoritesScreen(
    state: TravelFavoritesState,
    onEvent: (TravelFavoritesEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryShortcuts = listOf(
        FavoriteCategoryShortcut(FavoriteType.HOTEL.name, stringResource(R.string.hotels_label), HotelIcon),
        FavoriteCategoryShortcut(FavoriteType.TOUR.name, stringResource(R.string.tours_label), TicketIcon)
    )

    // Estado local para filtrar en tiempo real por la barra de búsqueda
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = if (searchQuery.isBlank()) {
        state.allFavorites
    } else {
        state.allFavorites.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.location.contains(searchQuery, ignoreCase = true)
        }
    }

    val hotels = filteredList.filter { it.type.equals("HOTEL", ignoreCase = true) || it.type.equals("HOTELS", ignoreCase = true) }
    val tours = filteredList.filter { it.type.equals("TOUR", ignoreCase = true) || it.type.equals("TOURS", ignoreCase = true) }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Top Bar: Arrow + Search
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TravelIconButton(
                        icon = ArrowLeftIcon,
                        onClick = { onEvent(TravelFavoritesEvent.OnGoBackClick) },
                        backgroundColor = Color.Transparent,
                        iconColor = MaterialTheme.colorScheme.onSurface
                    )

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.search_favorites_placeholder),
                                style = MaterialTheme.typography.bodyMedium,
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
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Filters
                TravelFavoritesCategoryShortcuts(
                    categories = categoryShortcuts,
                    isEnabled = state.hasContent,
                    onCategoryClick = { categoryName ->
                        val selectedType = FavoriteType.entries.find { it.name.equals(categoryName, ignoreCase = true) } ?: FavoriteType.HOTEL
                        onEvent(TravelFavoritesEvent.OnCategorySelected(selectedType))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    state.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    state.isRestricted -> {
                        TravelEmptyFavorites(
                            title = stringResource(R.string.not_a_user_title),
                            subtitle = stringResource(R.string.not_a_user_subtitle),
                            buttonText = stringResource(R.string.sign_in_button),
                            onButtonClick = { onEvent(TravelFavoritesEvent.OnSignInClick) }
                        )
                    }

                   state.isEmpty || (searchQuery.isNotEmpty() && filteredList.isEmpty()) -> {
                        TravelEmptyFavorites(
                            title = stringResource(R.string.no_favorites_title),
                            subtitle = stringResource(R.string.no_favorites_subtitle),
                            buttonText = stringResource(R.string.go_back_button),
                            onButtonClick = { onEvent(TravelFavoritesEvent.OnGoBackClick) }
                        )
                    }

                    else -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {
                            if (tours.isNotEmpty()) {
                                FavoritesCarouselSection(
                                    title = stringResource(R.string.tours_label),
                                    items = tours,
                                    onSeeAllClick = {
                                        onEvent(TravelFavoritesEvent.OnCategorySelected(FavoriteType.TOUR))
                                    },
                                    onCardClick = { item ->
                                        onEvent(TravelFavoritesEvent.OnFavoriteItemClick(item.id, FavoriteType.TOUR))
                                    },
                                    onRemoveClick = { item ->
                                        onEvent(TravelFavoritesEvent.OnRemoveFavorite(item))
                                    }
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }

                            if (hotels.isNotEmpty()) {
                                FavoritesCarouselSection(
                                    title = stringResource(R.string.hotels_label),
                                    items = hotels,
                                    onSeeAllClick = {
                                        onEvent(TravelFavoritesEvent.OnCategorySelected(FavoriteType.HOTEL))
                                    },
                                    onCardClick = { item ->
                                        onEvent(TravelFavoritesEvent.OnFavoriteItemClick(item.id, FavoriteType.HOTEL))
                                    },
                                    onRemoveClick = { item ->
                                        onEvent(TravelFavoritesEvent.OnRemoveFavorite(item))
                                    }
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoritesCarouselSection(
    title: String,
    items: List<FavoriteItem>,
    onSeeAllClick: () -> Unit,
    onCardClick: (FavoriteItem) -> Unit,
    onRemoveClick: (FavoriteItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            TravelTextActionButton(
                text = stringResource(R.string.see_all_label),
                onClick = onSeeAllClick
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(items = items.take(3), key = { it.id }) { item ->
                FavoriteItemCard(
                    favoriteItem = item,
                    onCardClick = onCardClick,
                    onRemoveClick = onRemoveClick,
                    modifier = Modifier.width(180.dp)
                )
            }
        }
    }
}

// ============================================================================
// PREVIEWS
// ============================================================================

@Preview(showBackground = true, name = "Favorites Loaded Preview")
@Composable
private fun TravelFavoritesScreenLoadedPreview() {
    val mockFavorites = listOf(
        // === TOURS / TRIPS ===
        FavoriteItem(
            id = "1",
            title = "Mount Bromo",
            location = "Volcano in East Java",
            rating = 4.9,
            price = 150,
            imageUrl = "",
            type = "TOUR",
            addedAt = System.currentTimeMillis()
        ),
        FavoriteItem(
            id = "2",
            title = "Labengki Sombori",
            location = "Islands in Sulawesi",
            rating = 4.8,
            price = 250,
            imageUrl = "",
            type = "TOUR",
            addedAt = System.currentTimeMillis()
        ),
        FavoriteItem(
            id = "3",
            title = "Torres del Paine",
            location = "Patagonia, Chile",
            rating = 5.0,
            price = 400,
            imageUrl = "",
            type = "TOUR",
            addedAt = System.currentTimeMillis()
        ),

        // === HOTELS ===
        FavoriteItem(
            id = "4",
            title = "Grand Hotel Valparaíso",
            location = "Viña del Mar",
            rating = 4.8,
            price = 120,
            imageUrl = "",
            type = "HOTEL",
            addedAt = System.currentTimeMillis()
        ),
        FavoriteItem(
            id = "5",
            title = "Enjoy Viña del Mar",
            location = "Viña del Mar",
            rating = 4.6,
            price = 180,
            imageUrl = "",
            type = "HOTEL",
            addedAt = System.currentTimeMillis()
        )
    )

    Travelin2026ProjectLabTheme {
        TravelFavoritesScreen(
            state = TravelFavoritesState(
                isLoading = false,
                isAuthenticated = true,
                allFavorites = mockFavorites
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Favorites Empty Preview")
@Composable
private fun TravelFavoritesScreenEmptyPreview() {
    Travelin2026ProjectLabTheme {
        TravelFavoritesScreen(
            state = TravelFavoritesState(
                isLoading = false,
                isAuthenticated = true,
                allFavorites = emptyList()
            ),
            onEvent = {}
        )
    }
}
