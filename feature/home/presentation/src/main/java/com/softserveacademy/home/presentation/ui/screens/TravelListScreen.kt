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
import com.softserveacademy.core.domain.model.TravelItemType
import com.softserveacademy.core.presentation.design_system.components.TravelCardHorizontal
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
import com.softserveacademy.core.presentation.design_system.theme.ArrowLeftIcon
import com.softserveacademy.core.presentation.design_system.theme.Green70
import com.softserveacademy.core.presentation.design_system.theme.White100_Alpha70
import com.softserveacademy.home.presentation.R
import com.softserveacademy.home.presentation.model.TourUi
import com.softserveacademy.home.presentation.state.SectionState
import com.softserveacademy.home.presentation.viewmodel.HomeViewModel

/**
 * Screen that displays a vertical list of travel items (Hotels or Tours).
 *
 * @param type The type of items to display.
 * @param onBackClick Callback invoked when the back button is clicked.
 * @param onItemClick Callback invoked when an item is selected.
 * @param viewModel The ViewModel providing data.
 */
@Composable
fun TravelListScreen(
    type: TravelItemType,
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val titleRes = when (type) {
        TravelItemType.HOTEL -> R.string.hotels_recommendation_title
        TravelItemType.TOUR -> R.string.journey_together_title
    }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = titleRes),
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
            val sectionState = when (type) {
                TravelItemType.HOTEL -> state.hotelsRecommended
                TravelItemType.TOUR -> state.journeyTogether
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
                            Box(
                                modifier = Modifier.clickable { 
                                    val id = when (item) {
                                        is Hotel -> item.id?.toString() ?: ""
                                        is TourUi -> item.id
                                        else -> ""
                                    }
                                    onItemClick(id) 
                                }
                            ) {
                                when (item) {
                                    is Hotel -> TravelCardHorizontal(hotel = item)
                                    is TourUi -> {
                                        TravelCardHorizontal(
                                            title = item.title,
                                            location = item.location,
                                            rating = item.rating.toString(),
                                            price = item.price,
                                            imageUrl = item.imageUrl
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
