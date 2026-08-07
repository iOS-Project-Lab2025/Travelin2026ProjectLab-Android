package com.softserveacademy.home.presentation.viewmodel

import com.softserveacademy.core.domain.model.Airline
import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.model.CabinClass
import com.softserveacademy.core.domain.model.Destination
import com.softserveacademy.core.domain.model.Flight
import com.softserveacademy.core.domain.model.FlightBooking
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.Ticket
import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.domain.model.TourCategory
import com.softserveacademy.core.domain.model.Trip
import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.home.domain.usecases.GetJourneyTogetherUseCase
import com.softserveacademy.home.domain.usecases.GetRecommendedHotelsUseCase
import com.softserveacademy.home.domain.usecases.GetUpcomingTripUseCase
import com.softserveacademy.home.domain.usecases.GetUserProfileUseCase
import com.softserveacademy.home.presentation.model.UpcomingTripUi
import com.softserveacademy.home.presentation.model.UserProfileUi
import com.softserveacademy.home.presentation.state.SectionState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.time.Duration.Companion.hours
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val getUserProfileUseCase = mockk<GetUserProfileUseCase>()
    private val getUpcomingTripUseCase = mockk<GetUpcomingTripUseCase>()
    private val getJourneyTogetherUseCase = mockk<GetJourneyTogetherUseCase>()
    private val getRecommendedHotelsUseCase = mockk<GetRecommendedHotelsUseCase>()

    private val profile = UserProfile(
        firstName = "John",
        lastName = "Doe",
        points = 100,
        avatarUrl = "https://example.com/avatar.png",
        location = "Santiago"
    )

    private val flight = Flight(
        id = "flight_001",
        airline = Airline("LA", "LATAM Airlines", "https://example.com/logo.png"),
        flightNumber = "LA500",
        origin = Airport("SCL", "Arturo Merino Benítez", "Santiago", "Chile"),
        destination = Airport("CDG", "Charles de Gaulle", "Paris", "France"),
        departureTime = 1000L,
        arrivalTime = 2000L,
        duration = 13.hours,
        cabinClass = CabinClass.ECONOMY
    )

    private val ticket = Ticket(ticketNumber = "TK-123", passengerName = "John Doe")
    private val flightBooking = FlightBooking(
        bookingId = "booking_001",
        flights = listOf(flight),
        tickets = listOf(ticket),
        confirmationCode = "ABC123",
        status = BookingStatus.COMPLETED
    )

    private val destination = Destination(
        id = "dest_001",
        imageUrl = "https://example.com/image.png",
        name = "Paris",
        location = "France",
        rating = 4.7,
        pricePerPax = 1500.0,
        currency = "EUR",
        durationLabel = "4D3N"
    )

    private val trip = Trip(
        id = "trip_001",
        destination = destination,
        startDate = 1000L,
        endDate = 2000L,
        flights = listOf(flightBooking),
        hotel = null,
        tours = null
    )

    private val tours = listOf(
        Tour(
            id = "tour_001",
            title = "Eiffel Tower",
            description = "Guided tour",
            location = "Paris, France",
            imageList = listOf("https://example.com/tour.png"),
            duration = 3.hours,
            price = 120.0,
            rating = 4.8,
            category = TourCategory.CULTURE
        )
    )

    private val hotels = listOf(
        Hotel(
            name = "Hotel A",
            address = "Paris, France",
            imageList = emptyList()
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given all success when init then all sections are Success`() = runTest {
        coEvery { getUserProfileUseCase() } returns Result.success(profile)
        coEvery { getUpcomingTripUseCase() } returns Result.success(trip)
        coEvery { getJourneyTogetherUseCase() } returns Result.success(tours)
        coEvery { getRecommendedHotelsUseCase() } returns Result.success(hotels)

        val viewModel = HomeViewModel(
            getUserProfileUseCase,
            getUpcomingTripUseCase,
            getJourneyTogetherUseCase,
            getRecommendedHotelsUseCase
        )

        val state = viewModel.state.value
        assertTrue(state.userProfile is SectionState.Success)
        assertTrue(state.upcomingTrip is SectionState.Success)
        assertTrue(state.journeyTogether is SectionState.Success)
        assertTrue(state.hotelsRecommended is SectionState.Success)
    }

    @Test
    fun `given profile fails when init then userProfile is Error`() = runTest {
        coEvery { getUserProfileUseCase() } returns Result.failure(Exception("Profile error"))
        coEvery { getUpcomingTripUseCase() } returns Result.success(trip)
        coEvery { getJourneyTogetherUseCase() } returns Result.success(tours)
        coEvery { getRecommendedHotelsUseCase() } returns Result.success(hotels)

        val viewModel = HomeViewModel(
            getUserProfileUseCase,
            getUpcomingTripUseCase,
            getJourneyTogetherUseCase,
            getRecommendedHotelsUseCase
        )

        val state = viewModel.state.value
        assertTrue(state.userProfile is SectionState.Error)
        assertEquals("Profile error", (state.userProfile as SectionState.Error).message)
    }

    @Test
    fun `given trip is null when init then upcomingTrip is Empty`() = runTest {
        coEvery { getUserProfileUseCase() } returns Result.success(profile)
        coEvery { getUpcomingTripUseCase() } returns Result.success(null)
        coEvery { getJourneyTogetherUseCase() } returns Result.success(tours)
        coEvery { getRecommendedHotelsUseCase() } returns Result.success(hotels)

        val viewModel = HomeViewModel(
            getUserProfileUseCase,
            getUpcomingTripUseCase,
            getJourneyTogetherUseCase,
            getRecommendedHotelsUseCase
        )

        val state = viewModel.state.value
        assertTrue(state.upcomingTrip is SectionState.Empty)
    }

    @Test
    fun `given trip fails when init then upcomingTrip is Error`() = runTest {
        coEvery { getUserProfileUseCase() } returns Result.success(profile)
        coEvery { getUpcomingTripUseCase() } returns Result.failure(Exception("Trip error"))
        coEvery { getJourneyTogetherUseCase() } returns Result.success(tours)
        coEvery { getRecommendedHotelsUseCase() } returns Result.success(hotels)

        val viewModel = HomeViewModel(
            getUserProfileUseCase,
            getUpcomingTripUseCase,
            getJourneyTogetherUseCase,
            getRecommendedHotelsUseCase
        )

        val state = viewModel.state.value
        assertTrue(state.upcomingTrip is SectionState.Error)
        assertEquals("Trip error", (state.upcomingTrip as SectionState.Error).message)
    }

    @Test
    fun `given journey fails when init then journeyTogether is Error`() = runTest {
        coEvery { getUserProfileUseCase() } returns Result.success(profile)
        coEvery { getUpcomingTripUseCase() } returns Result.success(trip)
        coEvery { getJourneyTogetherUseCase() } returns Result.failure(Exception("Tours error"))
        coEvery { getRecommendedHotelsUseCase() } returns Result.success(hotels)

        val viewModel = HomeViewModel(
            getUserProfileUseCase,
            getUpcomingTripUseCase,
            getJourneyTogetherUseCase,
            getRecommendedHotelsUseCase
        )

        val state = viewModel.state.value
        assertTrue(state.journeyTogether is SectionState.Error)
        assertEquals("Tours error", (state.journeyTogether as SectionState.Error).message)
    }

    @Test
    fun `given hotels fail when init then hotelsRecommended is Error`() = runTest {
        coEvery { getUserProfileUseCase() } returns Result.success(profile)
        coEvery { getUpcomingTripUseCase() } returns Result.success(trip)
        coEvery { getJourneyTogetherUseCase() } returns Result.success(tours)
        coEvery { getRecommendedHotelsUseCase() } returns Result.failure(Exception("Hotels error"))

        val viewModel = HomeViewModel(
            getUserProfileUseCase,
            getUpcomingTripUseCase,
            getJourneyTogetherUseCase,
            getRecommendedHotelsUseCase
        )

        val state = viewModel.state.value
        assertTrue(state.hotelsRecommended is SectionState.Error)
        assertEquals("Hotels error", (state.hotelsRecommended as SectionState.Error).message)
    }

    @Test
    fun `given all fail when init then all sections are Error`() = runTest {
        coEvery { getUserProfileUseCase() } returns Result.failure(Exception("Profile error"))
        coEvery { getUpcomingTripUseCase() } returns Result.failure(Exception("Trip error"))
        coEvery { getJourneyTogetherUseCase() } returns Result.failure(Exception("Tours error"))
        coEvery { getRecommendedHotelsUseCase() } returns Result.failure(Exception("Hotels error"))

        val viewModel = HomeViewModel(
            getUserProfileUseCase,
            getUpcomingTripUseCase,
            getJourneyTogetherUseCase,
            getRecommendedHotelsUseCase
        )

        val state = viewModel.state.value
        assertTrue(state.userProfile is SectionState.Error)
        assertTrue(state.upcomingTrip is SectionState.Error)
        assertTrue(state.journeyTogether is SectionState.Error)
        assertTrue(state.hotelsRecommended is SectionState.Error)
    }

    @Test
    fun `given profile fails with no message then uses default message`() = runTest {
        coEvery { getUserProfileUseCase() } returns Result.failure(Exception())
        coEvery { getUpcomingTripUseCase() } returns Result.success(trip)
        coEvery { getJourneyTogetherUseCase() } returns Result.success(tours)
        coEvery { getRecommendedHotelsUseCase() } returns Result.success(hotels)

        val viewModel = HomeViewModel(
            getUserProfileUseCase,
            getUpcomingTripUseCase,
            getJourneyTogetherUseCase,
            getRecommendedHotelsUseCase
        )

        val state = viewModel.state.value
        assertTrue(state.userProfile is SectionState.Error)
        assertEquals("Failed to load profile", (state.userProfile as SectionState.Error).message)
    }

    @Test
    fun `given success when init then userProfile is mapped correctly`() = runTest {
        coEvery { getUserProfileUseCase() } returns Result.success(profile)
        coEvery { getUpcomingTripUseCase() } returns Result.success(trip)
        coEvery { getJourneyTogetherUseCase() } returns Result.success(tours)
        coEvery { getRecommendedHotelsUseCase() } returns Result.success(hotels)

        val viewModel = HomeViewModel(
            getUserProfileUseCase,
            getUpcomingTripUseCase,
            getJourneyTogetherUseCase,
            getRecommendedHotelsUseCase
        )

        val state = viewModel.state.value
        val userProfileUi = (state.userProfile as SectionState.Success<UserProfileUi>).data
        assertEquals("John Doe", userProfileUi.name)
        assertEquals(100, userProfileUi.points)
        assertEquals("https://example.com/avatar.png", userProfileUi.avatarUrl)
    }

    @Test
    fun `given success when init then upcomingTrip is mapped correctly`() = runTest {
        coEvery { getUserProfileUseCase() } returns Result.success(profile)
        coEvery { getUpcomingTripUseCase() } returns Result.success(trip)
        coEvery { getJourneyTogetherUseCase() } returns Result.success(tours)
        coEvery { getRecommendedHotelsUseCase() } returns Result.success(hotels)

        val viewModel = HomeViewModel(
            getUserProfileUseCase,
            getUpcomingTripUseCase,
            getJourneyTogetherUseCase,
            getRecommendedHotelsUseCase
        )

        val state = viewModel.state.value
        val upcomingTripUi = (state.upcomingTrip as SectionState.Success<UpcomingTripUi>).data
        assertEquals("Upcoming", upcomingTripUi.status)
        assertEquals("SCL", upcomingTripUi.originCode)
        assertEquals("CDG", upcomingTripUi.destinationCode)
        assertEquals("LATAM Airlines", upcomingTripUi.airline)
        assertEquals("Economy", upcomingTripUi.travelClass)
        assertEquals("booking_001", upcomingTripUi.bookingId)
        assertEquals(1, upcomingTripUi.passengerCount)
        assertEquals(1, upcomingTripUi.flightCount)
    }
}
