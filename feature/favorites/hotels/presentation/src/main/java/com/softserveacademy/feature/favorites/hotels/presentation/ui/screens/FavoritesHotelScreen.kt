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

@Composable
fun FavoritesHotelScreen(
    state: FavoriteHotelsState,
    onEvent: (FavoriteHotelsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val filterOptions = listOf(
        stringResource(id = R.string.filter_available),
        stringResource(id = R.string.filter_all),
        stringResource(id = R.string.filter_1_bed),
        stringResource(id = R.string.filter_2_beds)
    )

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

            // TopBar Header
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

            // Filter Chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = TravelinDimens.PaddingMedium),
                horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
            ) {
                items(filterOptions) { filter ->
                    TravelFilterChip(
                        text = filter,
                        isSelected = filter == state.selectedFilter,
                        onClick = { onEvent(FavoriteHotelsEvent.OnFilterSelected(filter)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(TravelinDimens.PaddingMedium))

            // Horizontal Hotel Card List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = TravelinDimens.PaddingMedium),
                verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
            ) {
                items(items = state.filteredHotels, key = { it.id }) { item ->
                    Box(
                        modifier = Modifier.clickable {
                            onEvent(FavoriteHotelsEvent.OnHotelClick(item))
                        }
                    ) {
                        TravelCardHorizontal(hotel = item.hotel)
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
                star = 4,
                pricePerNight = 25,
                image = listOf("https://picsum.photos/200"),
                imagesList = emptyList()
            )
        ),
        FavoriteHotel(
            id = "2",
            hotel = Hotel(
                name = "Hotel 2",
                address = "Krong Siem Reap",
                star = 5,
                pricePerNight = 25,
                image = listOf("https://picsum.photos/201"),
                imagesList = emptyList()
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
