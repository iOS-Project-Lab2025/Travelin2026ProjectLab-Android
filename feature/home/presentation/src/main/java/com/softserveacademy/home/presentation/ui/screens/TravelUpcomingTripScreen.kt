package com.softserveacademy.home.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.domain.model.Destination
import com.softserveacademy.core.presentation.design_system.theme.AngleLeftIcon
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.home.presentation.model.FlightBookingUi
import com.softserveacademy.home.presentation.model.HotelInfoUi
import com.softserveacademy.home.presentation.model.TicketUi
import com.softserveacademy.home.presentation.model.TripDetailUi
import com.softserveacademy.home.presentation.model.toTripDetailUi
import com.softserveacademy.home.presentation.state.SectionState
import com.softserveacademy.home.presentation.state.UpcomingTripState
import com.softserveacademy.home.presentation.ui.components.TravelNavigationBar
import com.softserveacademy.home.presentation.ui.components.TravelTripDetailContent
import com.softserveacademy.home.presentation.viewmodel.UpcomingTripViewModel
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@Composable
fun RootUpcomingTripScreen(
    onBackClick: () -> Unit,
    onTabClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: UpcomingTripViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTrip()
    }

    TravelUpcomingTripScreen(
        state = state,
        onBackClick = onBackClick,
        onTabClick = onTabClick,
        modifier = modifier
    )
}

@Composable
fun TravelUpcomingTripScreen(
    state: UpcomingTripState,
    onBackClick: () -> Unit,
    onTabClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = {
                    Text(
                        text = "Upcoming Trip",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = AngleLeftIcon,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(TravelinDimens.IconSizeLarge)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            TravelNavigationBar(
                selectedTab = 0,
                onTabClick = onTabClick
            )
        },
        modifier = modifier
    ) { innerPadding ->
        when (val tripState = state.trip) {
            is SectionState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is SectionState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tripState.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            is SectionState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No upcoming trip found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            is SectionState.Success -> {
                val tripDetail = tripState.data.toTripDetailUi()
                TravelTripDetailContent(
                    tripDetail = tripDetail,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TravelUpcomingTripScreenPreview() {
    val mockTrip = TripDetailUi(
        tripId = "trip_001",
        destination = "Bali",
        startDate = "Sat, Nov 23",
        endDate = "Thu, Nov 28",
        flightBookings = listOf(
            FlightBookingUi(
                bookingId = "GA-880",
                flights = listOf(
                    com.softserveacademy.home.presentation.model.FlightUi(
                        flightNumber = "GA880",
                        airline = "Garuda Indonesia",
                        airlineLogo = "",
                        origin = Airport("CGK", "Soekarno-Hatta", "Jakarta", "Indonesia"),
                        destination = Airport("DPS", "Ngurah Rai", "Denpasar", "Indonesia"),
                        departureTime = "09:00",
                        arrivalTime = "10:30",
                        duration = 1.hours + 30.minutes,
                        cabinClass = "Economy"
                    )
                ),
                tickets = listOf(
                    TicketUi(
                        ticketNumber = "TK-123456",
                        passengerName = "John Doe",
                        seatNumber = "12A",
                        gate = "B15",
                        boardingGroup = "Group 2",
                        seatClass = "Economy"
                    ),
                    TicketUi(
                        ticketNumber = "TK-123457",
                        passengerName = "Jane Doe",
                        seatNumber = "12B",
                        gate = "B15",
                        boardingGroup = "Group 2",
                        seatClass = "Economy"
                    )
                ),
                confirmationCode = "ABC123",
                status = "Confirmed"
            )
        ),
        hotelInfo = HotelInfoUi(
            name = "Swiss-Belhotel Rainforest",
            roomType = "Deluxe Ocean View",
            checkIn = "Sat, Nov 23",
            checkOut = "Thu, Nov 28",
            guests = 2,
            confirmationCode = "HOTEL456"
        ),
        tourInfo = null
    )

    TravelUpcomingTripScreen(
        state = UpcomingTripState(trip = SectionState.Success(com.softserveacademy.core.domain.model.Trip(
            id = "trip_001",
            destination = Destination(
                id = "dest_001",
                imageUrl = "",
                name = "Bali",
                location = "Indonesia",
                rating = 4.8,
                pricePerPax = 1200.0,
                currency = "USD",
                durationLabel = "5D4N"
            ),
            startDate = 0L,
            endDate = 0L,
            flights = emptyList(),
            hotel = null,
            tours = null
        ))),
        onBackClick = {}
    )
}


