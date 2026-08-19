package com.softserveacademy.home.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.presentation.design_system.components.TravelCardHorizontal
import com.softserveacademy.core.presentation.design_system.components.TravelCardVertical
import com.softserveacademy.core.presentation.design_system.components.TravelCarousel
import com.softserveacademy.core.presentation.design_system.components.TravelTextActionButton
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.home.presentation.R
import com.softserveacademy.home.presentation.mockdata.PresentationMockData
import com.softserveacademy.home.presentation.model.TravelItemType
import com.softserveacademy.home.presentation.model.TourUi
import com.softserveacademy.home.presentation.navigation.HomeNavigationActions
import com.softserveacademy.home.presentation.state.HomeUiState
import com.softserveacademy.home.presentation.state.SectionState
import com.softserveacademy.home.presentation.ui.components.TravelBackground
import com.softserveacademy.home.presentation.ui.components.TravelIconsCard
import com.softserveacademy.core.presentation.ui.components.TravelNavigationBar
import com.softserveacademy.home.presentation.ui.components.TravelTextField
import com.softserveacademy.home.presentation.ui.components.TravelUpcomingTripCard
import com.softserveacademy.profile.presentation.ui.components.TravelUserProfileCard
import com.softserveacademy.home.presentation.viewmodel.HomeViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.softserveacademy.core.domain.model.RatePerParticipant
import com.softserveacademy.core.domain.util.formatPrice
import com.softserveacademy.core.presentation.design_system.components.TravelLoadingScreen

/**
 * Stateful wrapper for [TravelHomeScreen].
 *
 * <p>Manages the connection between the UI and the [HomeViewModel]. Collects the
 * [HomeUiState] from the ViewModel and delegates rendering to the stateless
 * [TravelHomeScreen] composable.</p>
 *
 * <p>This composable is intended to be used as the entry point for the Home screen
 * from navigation, while [TravelHomeScreen] can be used directly for previews or
 * testing with custom data.</p>
 *
 * @param actions Container for navigation callbacks used by the screen.
 * @param modifier Modifier to be applied to the root layout.
 * @param viewModel The [HomeViewModel] that provides the screen data. Defaults to a
 *   Hilt-injected instance.
 */
@Composable
fun RootHomeScreen(
    modifier: Modifier = Modifier,
    actions: HomeNavigationActions = HomeNavigationActions(),
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var isSearchMode by remember { mutableStateOf(false) }

    val isInitialLoading = state.journeyTogether is SectionState.Loading ||
            state.hotelsRecommended is SectionState.Loading ||
            state.userProfile is SectionState.Loading ||
            state.upcomingTrip is SectionState.Loading

    if (isSearchMode) {
        DestinationSearchScreen(
            onBackClick = { isSearchMode = false },
            onItemClick = { id, type ->
                isSearchMode = false
                if (type == TravelItemType.HOTEL) {
                    actions.onHotelClick(id)
                } else {
                    actions.onTourClick(id)
                }
            }
        )
    } else if (isInitialLoading) {
        TravelLoadingScreen()
    } else {
        TravelHomeScreen(
            state = state,
            onHotelClick = actions.onHotelClick,
            onTourClick = actions.onTourClick,
            onFlightClick = actions.onFlightsClick,  
            onAccountClick = actions.onAccountClick,
            onFavoritesClick = actions.onFavoritesClick,
            onProfileClick = actions.onProfileClick,
            onJourneySeeAllClick = actions.onJourneySeeAllClick,
            onHotelsSeeAllClick = actions.onHotelsSeeAllClick,
            onUpcomingTripClick = actions.onUpcomingTripClick,
            onSearchClick = { isSearchMode = true }, // Activamos el modo búsqueda
            modifier = modifier
        )
    }
}

/**
 * Main home screen of the Travelin application.
 *
 * <p>Has a fixed top section with the user profile card, search bar, and icons card.
 * Below it, a scrollable section contains the upcoming trip card, a "Journey together"
 * carousel of vertical hotel cards, and a "Hotels recommendation for you" section with
 * cards arranged vertically. A bottom navigation bar provides access to different
 * sections of the app.</p>
 *
 * <p>This composable is stateless — all data is provided through a [HomeUiState], making it
 * suitable for previews and unit testing. Each section independently reports its loading,
 * success, or error state via [SectionState].</p>
 *
 * @param state The complete UI state for the home screen, including all sections.
 * @param onHotelClick Action to perform when a hotel card is tapped.
 * @param onAccountClick Action to perform when the account tab in the bottom bar is selected.
 * @param onJourneySeeAllClick Action to perform when the "See all" button in the "Journey together"
 *   section is tapped.
 * @param onHotelsSeeAllClick Action to perform when the "See all" button in the "Hotels
 *   recommendation for you" section is tapped.
 * @param onProfileClick Action to perform when the user profile card is tapped.
 * @param modifier Modifier to be applied to the root layout.
 */
@Composable
fun TravelHomeScreen(
    state: HomeUiState,
    onHotelClick: (String) -> Unit,
    onTourClick: (String) -> Unit,
    onAccountClick: () -> Unit,
    modifier: Modifier = Modifier,
    onFavoritesClick: () -> Unit = {},
    onFlightClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onJourneySeeAllClick: () -> Unit = {},
    onHotelsSeeAllClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onUpcomingTripClick: (String) -> Unit = {},
) {
    val userProfile = (state.userProfile as? SectionState.Success)?.data
    val upcomingTrip = (state.upcomingTrip as? SectionState.Success)?.data
    val tours = (state.journeyTogether as? SectionState.Success)?.data?.take(5) ?: emptyList()
    val hotels = (state.hotelsRecommended as? SectionState.Success)?.data?.take(5) ?: emptyList()

    val isToursLoading = state.journeyTogether is SectionState.Loading
    val isHotelsLoading = state.hotelsRecommended is SectionState.Loading
    val toursError = (state.journeyTogether as? SectionState.Error)?.message
    val hotelsError = (state.hotelsRecommended as? SectionState.Error)?.message

    Scaffold(
        bottomBar = {
            TravelNavigationBar(
                selectedTab = 0,
                onTabClick = { index ->
                    when (index) {
                        2 -> onFavoritesClick()
                        3 -> onAccountClick()
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = modifier.fillMaxSize().padding(bottom = innerPadding.calculateBottomPadding())) {
            TravelBackground()
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Fixed top section
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Spacer(Modifier.height(4.dp))
                    if (userProfile != null) {
                        TravelUserProfileCard(userProfile = userProfile, onClick = onProfileClick)
                        Spacer(Modifier.height(16.dp))
                    }
                    Box {
                        TravelTextField()
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { onSearchClick() }
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    TravelIconsCard(onFlightsClick = onFlightClick)
                }

                // Scrollable bottom section
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        if (upcomingTrip != null) {
                            Spacer(Modifier.height(16.dp))
                            TravelUpcomingTripCard(
                                trip = upcomingTrip,
                                onClick = { onUpcomingTripClick(upcomingTrip.bookingId) }
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        // "Journey together" section header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.journey_together_title),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            TravelTextActionButton(
                                text = stringResource(id = R.string.see_all_label),
                                onClick = onJourneySeeAllClick
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }

                    if (isToursLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    } else if (toursError != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = toursError,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        TravelCarousel(
                            items = tours
                        ) { tour ->
                            TravelCardVertical(
                                title = tour.title,
                                location = tour.location,
                                rating = tour.rating.toString(),
                                price = "$ ${formatPrice(tour.rates.adults)}",
                                duration = tour.duration,
                                imageUrl = tour.imageUrl ?: "",
                                onClick = { onTourClick(tour.id) }
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Spacer(Modifier.height(16.dp))

                        // "Hotels recommendation for you" section header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.hotels_recommendation_title),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            TravelTextActionButton(
                                text = stringResource(id = R.string.see_all_label),
                                onClick = onHotelsSeeAllClick
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        if (isHotelsLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            }
                        } else if (hotelsError != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = hotelsError,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        } else {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                hotels.forEach { hotel ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onHotelClick(hotel.id) }
                                    ) {
                                        TravelCardHorizontal(
                                            title = hotel.name,
                                            address = hotel.address,
                                            starRating = hotel.starCategory,
                                            ratingText = "${hotel.starCategory}-star hotel",
                                            price = "$${formatPrice(hotel.pricePerNight)}",
                                            priceSuffix = "/night",
                                            imageUrl = hotel.imageList.firstOrNull() ?: ""
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

/**
 * Preview of the [TravelHomeScreen] composable.
 *
 * <p>Displays the home screen with sample data inside the app theme. All sections are
 * pre-populated with [SectionState.Success] using mock data from [PresentationMockData].</p>
 */
@Preview
@Composable
private fun TravelHomeScreenPreview() {
    Travelin2026ProjectLabTheme {
        TravelHomeScreen(
            state = HomeUiState(
                userProfile = SectionState.Success(PresentationMockData.userProfile),
                upcomingTrip = SectionState.Success(PresentationMockData.upcomingTrip),
                journeyTogether = SectionState.Success(
                    listOf(
                        TourUi(
                            id = "1",
                            title = "Mount Bromo Sunrise",
                            imageUrl = "https://picsum.photos/id/1015/800/600",
                            location = "East Java",
                            rating = 4.9,
                            rates = RatePerParticipant(adults = 150.0),
                            duration = "1 day"
                        ),
                        TourUi(
                            id = "2",
                            title = "Bali Cultural Dance",
                            imageUrl = "https://picsum.photos/id/1016/800/600",
                            location = "Ubud, Bali",
                            rating = 4.7,
                            rates = RatePerParticipant(adults = 45.0),
                            duration = "4 hours"
                        )
                    )
                ),
                hotelsRecommended = SectionState.Success(
                    listOf(
                        Hotel(
                            id = "1",
                            name = "Koh Rong Samloem Resort",
                            address = "Koh Rong Samloem, Cambodia",
                            starCategory = 5,
                            reviewRating = 4.8,
                            imageList = listOf("https://picsum.photos/id/10/800/600"),
                        ),
                        Hotel(
                            id = "2",
                            name = "Sunset Paradise",
                            address = "Phuket, Thailand",
                            starCategory = 4,
                            reviewRating = 4.6,
                            imageList = listOf("https://picsum.photos/id/11/800/600"),
                        ),
                        Hotel(
                            id = "3",
                            name = "Swiss Mountain Lodge",
                            address = "Zermatt, Switzerland",
                            starCategory = 5,
                            reviewRating = 4.9,
                            imageList = listOf("https://picsum.photos/id/12/800/600"),
                        ),
                        Hotel(
                            id = "4",
                            name = "Ocean Breeze",
                            address = "Bali, Indonesia",
                            starCategory = 4,
                            reviewRating = 4.7,
                            imageList = listOf("https://picsum.photos/id/13/800/600"),
                        ),
                        Hotel(
                            id = "5",
                            name = "The Grand Palace",
                            address = "Paris, France",
                            starCategory = 5,
                            reviewRating = 4.9,
                            imageList = listOf("https://picsum.photos/id/14/800/600"),
                        )
                    )
                )
            ),
            onHotelClick = {},
            onTourClick = {},
            onAccountClick = {}
        )
    }
}
