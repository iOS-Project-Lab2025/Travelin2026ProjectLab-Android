package com.softserveacademy.home.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.core.domain.model.Airline
import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.domain.model.BookingContactInfo
import com.softserveacademy.core.domain.model.BookingGuests
import com.softserveacademy.core.domain.model.BookingParticipants
import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.model.CabinClass
import com.softserveacademy.core.domain.model.Destination
import com.softserveacademy.core.domain.model.Flight
import com.softserveacademy.core.domain.model.FlightBooking
import com.softserveacademy.core.domain.model.HotelBooking
import com.softserveacademy.core.domain.model.HotelBookingPrice
import com.softserveacademy.core.domain.model.Ticket
import com.softserveacademy.core.domain.model.TourBooking
import com.softserveacademy.core.domain.model.TourBookingPrice
import com.softserveacademy.core.domain.model.Trip
import com.softserveacademy.core.presentation.design_system.components.util.reusable_icons.TravelArrowIconButton
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.home.presentation.model.toTripDetailUi
import com.softserveacademy.home.presentation.state.SectionState
import com.softserveacademy.home.presentation.state.UpcomingTripState
import com.softserveacademy.core.presentation.ui.components.TravelNavigationBar
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
                        TravelArrowIconButton {onBackClick()  }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
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
        Box(modifier = Modifier.fillMaxSize()) {
           // TravelBackground()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (val tripState = state.trip) {
                    is SectionState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is SectionState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
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
                            modifier = Modifier.fillMaxSize(),
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
                        TravelTripDetailContent(tripDetail = tripDetail)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TravelUpcomingTripScreenPreview() {
    val parseMillis = { pattern: String, value: String ->
        java.text.SimpleDateFormat(pattern, java.util.Locale.US).parse(value)!!.time
    }

    val mockTrip = Trip(
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
        startDate = parseMillis("yyyy-MM-dd", "2024-11-23"),
        endDate = parseMillis("yyyy-MM-dd", "2024-11-28"),
        flights = listOf(
            FlightBooking(
                bookingId = "GA-880",
                userId = "user_001",
                flights = listOf(
                    Flight(
                        id = "fl_001",
                        airline = Airline("GA", "Garuda Indonesia", ""),
                        flightNumber = "GA880",
                        origin = Airport("CGK", "Soekarno-Hatta", "Jakarta", "Indonesia"),
                        destination = Airport("DPS", "Ngurah Rai", "Denpasar", "Indonesia"),
                        departureTime = parseMillis("yyyy-MM-dd'T'HH:mm", "2024-11-23T09:00"),
                        arrivalTime = parseMillis("yyyy-MM-dd'T'HH:mm", "2024-11-23T10:30"),
                        duration = 1.hours + 30.minutes,
                        cabinClass = CabinClass.ECONOMY
                    )
                ),
                passengers = emptyList(),
                tickets = listOf(
                    Ticket(
                        ticketNumber = "TK-123456",
                        passengerName = "John Doe",
                        flightId = "fl_001",
                        flightNumber = "GA880",
                        originCode = "CGK",
                        destinationCode = "DPS",
                        seatNumber = "12A",
                        gate = "B15",
                        boardingGroup = "Group 2",
                        cabinClass = CabinClass.ECONOMY
                    ),
                    Ticket(
                        ticketNumber = "TK-123457",
                        passengerName = "Jane Doe",
                        flightId = "fl_001",
                        flightNumber = "GA880",
                        originCode = "CGK",
                        destinationCode = "DPS",
                        seatNumber = "12B",
                        gate = "B15",
                        boardingGroup = "Group 2",
                        cabinClass = CabinClass.ECONOMY
                    )
                ),
                confirmationCode = "ABC123",
                status = BookingStatus.COMPLETED,
                totalAmount = 0.0,
                currencyCode = "USD",
                contactInfo = BookingContactInfo(firstName = "John", lastName = "Doe"),
                createdAt = parseMillis("yyyy-MM-dd", "2024-11-01")
            )
        ),
        hotel = HotelBooking(
            bookingId = "HB-456",
            hotelId = "hotel_001",
            roomId = "room_001",
            checkIn = parseMillis("yyyy-MM-dd", "2024-11-23"),
            checkOut = parseMillis("yyyy-MM-dd", "2024-11-28"),
            guests = BookingGuests(adults = 2),
            price = HotelBookingPrice(ratePerNight = 100, roomSubtotal = 500, taxes = 0, fees = 0, total = 500),
            status = BookingStatus.COMPLETED,
            confirmationCode = "HOTEL456",
            createdAt = parseMillis("yyyy-MM-dd", "2024-11-01"),
            contactInfo = BookingContactInfo(firstName = "John", lastName = "Doe")
        ),
        tours = listOf(
            TourBooking(
                bookingId = "TB-001",
                userId = "user_001",
                tourId = "tour_001",
                startDate = parseMillis("yyyy-MM-dd", "2024-11-24"),
                endDate = parseMillis("yyyy-MM-dd", "2024-11-24"),
                participants = BookingParticipants(adults = 2),
                price = TourBookingPrice(
                    ratePerAdult = 50.0,
                    ratePerChildren = 0.0,
                    ratePerInfant = 0.0,
                    subtotal = 100,
                    total = 100
                ),
                confirmationCode = "TOUR789",
                status = BookingStatus.COMPLETED,
                createdAt = parseMillis("yyyy-MM-dd", "2024-11-01")
            )
        )
    )
    Travelin2026ProjectLabTheme(darkTheme = true) {
        TravelUpcomingTripScreen(
            state = UpcomingTripState(trip = SectionState.Success(mockTrip)),
            onBackClick = {}
        )
    }

}


