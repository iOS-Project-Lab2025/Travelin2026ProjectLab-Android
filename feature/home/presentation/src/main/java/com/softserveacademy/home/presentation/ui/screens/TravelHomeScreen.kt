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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.softserveacademy.core.presentation.design_system.components.TravelCarousel
import com.softserveacademy.core.presentation.design_system.components.TravelTextActionButton
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.home.presentation.R
import com.softserveacademy.home.presentation.mockdata.PresentationMockData
import com.softserveacademy.home.presentation.model.UserProfileUi
import com.softserveacademy.home.presentation.ui.components.TravelBackground
import com.softserveacademy.home.presentation.ui.components.TravelIconsCard
import com.softserveacademy.home.presentation.ui.components.TravelNavigationBar
import com.softserveacademy.home.presentation.ui.components.TravelTextField
import com.softserveacademy.home.presentation.ui.components.TravelUpcomingTripCard
import com.softserveacademy.home.presentation.ui.components.TravelUserProfileCard
import com.softserveacademy.home.presentation.viewmodel.HomeViewModel

/**
 * Stateful wrapper for [TravelHomeScreen].
 *
 * <p>Manages the connection between the UI and the [HomeViewModel]. Collects the hotel data
 * from the ViewModel's state flow and delegates rendering to the stateless [TravelHomeScreen]
 * composable.</p>
 *
 * <p>This composable is intended to be used as the entry point for the Home screen from
 * navigation, while [TravelHomeScreen] can be used directly for previews or testing with
 * custom data.</p>
 *
 * @param onHotelClick Action to perform when a hotel card is tapped.
 * @param onAccountClick Action to perform when the account tab in the bottom bar is selected.
 * @param onJourneySeeAllClick Action to perform when the "See all" button in the "Journey together"
 *   section is tapped.
 * @param onHotelsSeeAllClick Action to perform when the "See all" button in the "Hotels
 *   recommendation for you" section is tapped.
 * @param userProfile The user profile data to display at the top of the screen.
 * @param modifier Modifier to be applied to the root layout.
 * @param viewModel The [HomeViewModel] that provides the screen data. Defaults to a
 *   Hilt-injected instance.
 */
@Composable
fun RootHomeScreen(
    onHotelClick: (Hotel) -> Unit,
    onAccountClick: () -> Unit,
    onJourneySeeAllClick: () -> Unit = {},
    onHotelsSeeAllClick: () -> Unit = {},
    userProfile: UserProfileUi = PresentationMockData.userProfile,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val hotels by viewModel.hotels.collectAsState()

    TravelHomeScreen(
        hotels = hotels,
        userProfile = userProfile,
        onHotelClick = onHotelClick,
        onAccountClick = onAccountClick,
        onJourneySeeAllClick = onJourneySeeAllClick,
        onHotelsSeeAllClick = onHotelsSeeAllClick,
        modifier = modifier
    )
}

/**
 * Main home screen of the Travelin application.
 *
 * <p>Displays a user profile card at the top, followed by a search bar, an upcoming trip card,
 * an icons card for quick actions, a "Journey together" carousel of vertical hotel cards, and a
 * "Hotels recommendation for you" section with horizontal cards. A bottom navigation bar provides
 * access to different sections of the app.</p>
 *
 * <p>This composable is stateless — all data is provided through parameters, making it
 * suitable for previews and unit testing.</p>
 *
 * @param hotels The list of [Hotel] objects to display in the carousel and recommendation sections.
 * @param userProfile The user profile data to display at the top of the screen.
 * @param onHotelClick Action to perform when a hotel card is tapped.
 * @param onAccountClick Action to perform when the account tab in the bottom bar is selected.
 * @param onJourneySeeAllClick Action to perform when the "See all" button in the "Journey together"
 *   section is tapped.
 * @param onHotelsSeeAllClick Action to perform when the "See all" button in the "Hotels
 *   recommendation for you" section is tapped.
 * @param modifier Modifier to be applied to the root layout.
 */
@Composable
fun TravelHomeScreen(
    hotels: List<Hotel>,
    userProfile: UserProfileUi,
    onHotelClick: (Hotel) -> Unit,
    onAccountClick: () -> Unit,
    onJourneySeeAllClick: () -> Unit = {},
    onHotelsSeeAllClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Scaffold(
        bottomBar = {
            TravelNavigationBar(
                selectedTab = 0,
                onTabClick = { index ->
                    if (index == 3) onAccountClick()
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = modifier.fillMaxSize().padding(innerPadding)) {
            TravelBackground()
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(16.dp))
                TravelUserProfileCard(userProfile = userProfile)
                Spacer(Modifier.height(16.dp))
                TravelTextField()
                Spacer(Modifier.height(16.dp))
                TravelUpcomingTripCard(trip = PresentationMockData.upcomingTrip)
                Spacer(Modifier.height(16.dp))
                Spacer(Modifier.height(16.dp))
                TravelIconsCard()
                Spacer(Modifier.height(16.dp))

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
                TravelCarousel(
                    packages = hotels,
                    onHotelClick = onHotelClick
                )
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
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(hotels) { hotel ->
                        Box(
                            modifier = Modifier.clickable { onHotelClick(hotel) }
                        ) {
                            TravelCardHorizontal(hotel = hotel)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Preview of the [TravelHomeScreen] composable.
 *
 * <p>Displays the home screen with sample hotel data inside the app theme.</p>
 */
@Preview
@Composable
private fun TravelHomeScreenPreview() {
    Travelin2026ProjectLabTheme() {
        TravelHomeScreen(
            hotels = listOf(
                Hotel(
                    id = 1,
                    name = "Koh Rong Samloem Resort",
                    address = "Koh Rong Samloem, Cambodia",
                    star = 5,
                    userRating = 4.8,
                    pricePerNight = 400,
                    image = listOf("https://picsum.photos/id/10/800/600")
                ),
                Hotel(
                    id = 2,
                    name = "Sunset Paradise",
                    address = "Phuket, Thailand",
                    star = 4,
                    userRating = 4.6,
                    pricePerNight = 280,
                    image = listOf("https://picsum.photos/id/11/800/600")
                ),
                Hotel(
                    id = 3,
                    name = "Swiss Mountain Lodge",
                    address = "Zermatt, Switzerland",
                    star = 5,
                    userRating = 4.9,
                    pricePerNight = 650,
                    image = listOf("https://picsum.photos/id/12/800/600")
                ),
                Hotel(
                    id = 4,
                    name = "Ocean Breeze",
                    address = "Bali, Indonesia",
                    star = 4,
                    userRating = 4.7,
                    pricePerNight = 320,
                    image = listOf("https://picsum.photos/id/13/800/600")
                ),
                Hotel(
                    id = 5,
                    name = "The Grand Palace",
                    address = "Paris, France",
                    star = 5,
                    userRating = 4.9,
                    pricePerNight = 720,
                    image = listOf("https://picsum.photos/id/14/800/600")
                )
            ),
            onHotelClick = {},
            onAccountClick = {},
            userProfile = PresentationMockData.userProfile
        )
    }
}
