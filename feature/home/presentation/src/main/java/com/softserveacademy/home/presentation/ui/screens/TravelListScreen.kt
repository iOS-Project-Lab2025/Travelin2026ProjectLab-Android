package com.softserveacademy.home.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.presentation.design_system.components.TravelCardHorizontal
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
import com.softserveacademy.core.presentation.design_system.theme.ArrowLeftIcon
import com.softserveacademy.core.presentation.design_system.theme.Green70
import com.softserveacademy.core.presentation.design_system.theme.White100_Alpha70
import com.softserveacademy.home.presentation.R
import com.softserveacademy.home.presentation.model.TravelItemType
import com.softserveacademy.home.presentation.state.SectionState
import com.softserveacademy.home.presentation.viewmodel.HomeViewModel

/**
 * Screen that displays a vertical list of travel items (Hotels or Tours).
 *
 * @param type The type of travel items to display.
 * @param onBackClick Callback invoked when the back button is clicked.
 * @param onItemClick Callback invoked when a travel item is selected.
 * @param viewModel The ViewModel providing data. Defaults to a Hilt-injected instance.
 */
@Composable
fun TravelListScreen(
    type: TravelItemType,
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = {
                    Text(
                        text = if (type == TravelItemType.HOTEL) {
                            stringResource(id = R.string.hotels_recommendation_title)
                        } else {
                            stringResource(id = R.string.journey_together_title)
                        },
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    TravelIconButton(
                        icon = ArrowLeftIcon,
                        onClick = onBackClick,
                        iconColor = Green70,
                        backgroundColor = White100_Alpha70,
                        contentDescription = "Back button",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val sectionState = if (type == TravelItemType.HOTEL) {
                state.hotelsRecommended
            } else {
                state.journeyTogether
            }

            when (sectionState) {
                is SectionState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is SectionState.Error -> {
                    Text(
                        text = sectionState.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is SectionState.Empty -> {
                    Text(
                        text = "No items found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is SectionState.Success -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(sectionState.data) { item ->
                            val id: String
                            val title: String
                            val address: String
                            val rating: Int
                            val ratingText: String
                            val price: String
                            val priceSuffix: String?
                            val imageUrl: String

                            if (item is Hotel) {
                                id = item.id
                                title = item.name
                                address = item.address
                                rating = item.starCategory
                                ratingText = "${item.starCategory}-star hotel"
                                price = "\$${item.pricePerNight}"
                                priceSuffix = "/night"
                                imageUrl = item.imageList.firstOrNull() ?: ""
                            } else {
                                // Assuming item is TourUi
                                val tour = item as com.softserveacademy.home.presentation.model.TourUi
                                id = tour.id
                                title = tour.title
                                address = tour.location
                                rating = tour.rating.toInt() // TODO: Stars and user rating it's different, maybe make new cards?
                                ratingText = tour.rating.toString()
                                price = "$ ${tour.rates.adults}"
                                priceSuffix = null
                                imageUrl = tour.imageUrl ?: ""
                            }

                            Box(
                                modifier = Modifier.clickable { onItemClick(id) }
                            ) {
                                TravelCardHorizontal(
                                    title = title,
                                    address = address,
                                    starRating = rating,
                                    ratingText = ratingText,
                                    price = price,
                                    priceSuffix = priceSuffix,
                                    imageUrl = imageUrl
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
