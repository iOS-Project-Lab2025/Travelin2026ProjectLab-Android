package com.softserveacademy.feature.favorites.tours.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.core.presentation.design_system.components.TravelCardHorizontal
import com.softserveacademy.core.presentation.design_system.components.TravelFilterChip
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
import com.softserveacademy.core.presentation.design_system.theme.ArrowLeftIcon
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.favorites.tours.presentation.events.FavoritesToursEffect
import com.softserveacademy.feature.favorites.tours.presentation.events.FavoritesToursEvent
import com.softserveacademy.feature.favorites.tours.presentation.states.FavoritesToursState
import com.softserveacademy.feature.favorites.tours.presentation.viewmodel.FavoritesToursViewModel

@Composable
fun RootFavoritesToursScreen(
    onBackClick: () -> Unit,
    onTourClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesToursViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is FavoritesToursEffect.NavigateToTourDetail -> onTourClick(effect.tourId)
                FavoritesToursEffect.NavigateBack -> onBackClick()
            }
        }
    }

    FavoritesToursScreen(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun FavoritesToursScreen(
    state: FavoritesToursState,
    onEvent: (FavoritesToursEvent) -> Unit,
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = TravelinDimens.PaddingMedium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TravelIconButton(
                    icon = ArrowLeftIcon,
                    onClick = { onEvent(FavoritesToursEvent.OnBackClick) },
                    backgroundColor = Color.Transparent,
                    iconColor = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Favorite Tours",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = TravelinDimens.PaddingSmall)
                )
            }

            Spacer(modifier = Modifier.height(TravelinDimens.PaddingMedium))

            val filters = listOf("All", "Adventure", "Culture")
            LazyRow(
                contentPadding = PaddingValues(horizontal = TravelinDimens.PaddingMedium),
                horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
            ) {
                items(filters) { filter ->
                    TravelFilterChip(
                        text = filter,
                        isSelected = state.selectedFilter == filter,
                        onClick = { onEvent(FavoritesToursEvent.OnFilterSelected(filter)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(TravelinDimens.PaddingMedium))

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = TravelinDimens.PaddingMedium),
                verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
            ) {
                items(items = state.filteredTours, key = { it.id }) { item ->
                    val tour = item.tour
                    Box(
                        modifier = Modifier.clickable {
                            onEvent(FavoritesToursEvent.OnTourClick(item))
                        }
                    ) {
                        TravelCardHorizontal(
                            title = tour.title,
                            address = tour.location,
                            starRating = tour.rating.toInt(),
                            ratingText = tour.rating.toString(),
                            price = "",
                            priceSuffix = null,
                            imageUrl = tour.imageList.firstOrNull() ?: ""
                        )
                    }
                }
            }
        }
    }
}
