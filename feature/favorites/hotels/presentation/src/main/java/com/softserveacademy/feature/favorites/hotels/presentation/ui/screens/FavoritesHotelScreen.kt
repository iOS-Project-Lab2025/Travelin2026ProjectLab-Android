package com.softserveacademy.feature.favorites.hotels.presentation.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.presentation.design_system.components.TravelCardHorizontal
import com.softserveacademy.core.presentation.design_system.components.TravelFilterChip
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
import com.softserveacademy.core.presentation.design_system.theme.ArrowLeftIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.favorites.common.hotel.presentation.R
import com.softserveacademy.feature.favorites.hotels.domain.model.FavoriteHotel
import com.softserveacademy.feature.favorites.hotels.presentation.events.FavoriteHotelsEvent
import com.softserveacademy.feature.favorites.hotels.presentation.states.FavoriteHotelsState

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.feature.favorites.hotels.presentation.events.FavoriteHotelsEffect
import com.softserveacademy.feature.favorites.hotels.presentation.viewmodel.FavoriteHotelsViewModel

@Composable
fun RootFavoritesHotelScreen(
    onBackClick: () -> Unit,
    onHotelClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoriteHotelsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is FavoriteHotelsEffect.NavigateToHotelDetail -> onHotelClick(effect.hotelId)
                FavoriteHotelsEffect.NavigateBack -> onBackClick()
            }
        }
    }

    FavoritesHotelScreen(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun FavoritesHotelScreen(
    state: FavoriteHotelsState,
    onEvent: (FavoriteHotelsEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(TravelinDimens.PaddingSmall))

            // 1. Header (Ahora sí será visible)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = TravelinDimens.PaddingMedium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TravelIconButton(
                    icon = ArrowLeftIcon,
                    onClick = { onEvent(FavoriteHotelsEvent.OnBackClick) },
                    backgroundColor = Color.Transparent,
                    iconColor = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = stringResource(id = R.string.favorites_hotels_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = TravelinDimens.PaddingSmall)
                )
            }

            Spacer(modifier = Modifier.height(TravelinDimens.PaddingMedium))

            // 2. Filtros (Importante para la lógica del Backend)
            val filters = listOf("All", "Available", "1 Bed", "2 Beds")
            LazyRow(
                contentPadding = PaddingValues(horizontal = TravelinDimens.PaddingMedium),
                horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
            ) {
                items(filters) { filter ->
                    TravelFilterChip(
                        text = filter,
                        isSelected = state.selectedFilter == filter,
                        onClick = { onEvent(FavoriteHotelsEvent.OnFilterSelected(filter)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(TravelinDimens.PaddingMedium))

            // 3. Lista de Favoritos (Dentro de la misma Column)
            LazyColumn(
                modifier = Modifier.weight(1f), // Usamos weight para que ocupe el resto del espacio
                contentPadding = PaddingValues(horizontal = TravelinDimens.PaddingMedium),
                verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
            ) {
                items(items = state.filteredHotels, key = { it.id }) { item ->
                    val hotel = item.hotel
                    val displayPrice = hotel.rooms.firstOrNull()?.pricePerNight ?: 0

                    Box(
                        modifier = Modifier.clickable {
                            onEvent(FavoriteHotelsEvent.OnHotelClick(item))
                        }
                    ) {
                        TravelCardHorizontal(
                            title = hotel.name,
                            address = hotel.address,
                            starRating = hotel.starCategory,
                            ratingText = "${hotel.starCategory}-star hotel",
                            price = "$ $displayPrice",
                            priceSuffix = "/night",
                            imageUrl = hotel.imageList.firstOrNull() ?: ""
                        )
                    }
                }
            }
        }
    }
}

// ============================================================================
// PREVIEW
// ============================================================================

@Preview(showBackground = true)
@Composable
private fun FavoritesHotelScreenPreview() {
    val sampleHotels = listOf(
        FavoriteHotel(
            id = "1",
            hotel = Hotel(
                name = "Hotel 1",
                address = "Krong Siem Reap",
                starCategory = 4,
                imageList = listOf("https://picsum.photos/200"),
            )
        ),
        FavoriteHotel(
            id = "2",
            hotel = Hotel(
                name = "Hotel 2",
                address = "Krong Siem Reap",
                starCategory = 5,
                imageList = listOf("https://picsum.photos/201"),
            )
        )
    )

    Travelin2026ProjectLabTheme {
        FavoritesHotelScreen(
            state = FavoriteHotelsState(
                hotels = sampleHotels,
                filteredHotels = sampleHotels,
                selectedFilter = "Available"
            ),
            onEvent = {}
        )
    }
}
