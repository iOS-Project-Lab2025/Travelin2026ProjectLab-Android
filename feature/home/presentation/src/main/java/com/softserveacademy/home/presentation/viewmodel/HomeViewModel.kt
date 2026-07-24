package com.softserveacademy.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.domain.model.Trip
import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.home.domain.usecases.GetJourneyTogetherUseCase
import com.softserveacademy.home.domain.usecases.GetRecommendedHotelsUseCase
import com.softserveacademy.home.domain.usecases.GetUpcomingTripUseCase
import com.softserveacademy.home.domain.usecases.GetUserProfileUseCase
import com.softserveacademy.home.presentation.model.TourUi
import com.softserveacademy.home.presentation.model.UpcomingTripUi
import com.softserveacademy.home.presentation.model.UserProfileUi
import com.softserveacademy.home.presentation.state.HomeUiState
import com.softserveacademy.home.presentation.state.SectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.time.Duration

/**
 * ViewModel responsible for managing the state and business logic of the Home screen.
 *
 * Coordinates the loading of all sections displayed on the home dashboard:
 * user profile, upcoming trip, journey together destinations, and recommended hotels.
 * Each section is loaded independently and reports its own loading/success/error state
 * via [HomeUiState] with [SectionState].
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUpcomingTripUseCase: GetUpcomingTripUseCase,
    private val getJourneyTogetherUseCase: GetJourneyTogetherUseCase,
    private val getRecommendedHotelsUseCase: GetRecommendedHotelsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        loadAllSections()
    }

    private fun loadAllSections() {
        loadUserProfile()
        loadUpcomingTrip()
        loadJourneyTogether()
        loadHotels()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            getUserProfileUseCase()
                .onSuccess { profile ->
                    _state.update { it.copy(userProfile = SectionState.Success(profile.toUserProfileUi())) }
                }
                .onFailure { error ->
                    _state.update { it.copy(userProfile = SectionState.Error(error.message ?: "Failed to load profile")) }
                }
        }
    }

    private fun loadUpcomingTrip() {
        viewModelScope.launch {
            getUpcomingTripUseCase()
                .onSuccess { trip ->
                    val ui = trip?.toUpcomingTripUi()
                    if (ui != null) {
                        _state.update { it.copy(upcomingTrip = SectionState.Success(ui)) }
                    } else {
                        _state.update { it.copy(upcomingTrip = SectionState.Empty) }
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(upcomingTrip = SectionState.Error(error.message ?: "Failed to load trip")) }
                }
        }
    }

    private fun loadJourneyTogether() {
        viewModelScope.launch {
            getJourneyTogetherUseCase()
                .onSuccess { tours ->
                    _state.update {
                        it.copy(journeyTogether = SectionState.Success(tours.map { t -> t.toTourUi() }))
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(journeyTogether = SectionState.Error(error.message ?: "Failed to load tours")) }
                }
        }
    }

    private fun loadHotels() {
        viewModelScope.launch {
            getRecommendedHotelsUseCase()
                .onSuccess { hotels ->
                    _state.update { it.copy(hotelsRecommended = SectionState.Success(hotels)) }
                }
                .onFailure { error ->
                    _state.update { it.copy(hotelsRecommended = SectionState.Error(error.message ?: "Failed to load hotels")) }
                }
        }
    }

    // -- Mapper extensions --

    private fun UserProfile.toUserProfileUi(): UserProfileUi = UserProfileUi(
        name = "$firstName $lastName",
        points = points,
        avatarUrl = avatarUrl
    )

    private fun Trip.toUpcomingTripUi(): UpcomingTripUi? {
        val flightBooking = flights.firstOrNull() ?: return null
        val firstFlight = flightBooking.flights.firstOrNull() ?: return null
        val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return UpcomingTripUi(
            status = "Upcoming",
            date = dateFormat.format(Date(startDate)),
            originCode = firstFlight.origin.code,
            originTime = timeFormat.format(Date(firstFlight.departureTime)),
            destinationCode = firstFlight.destination.code,
            destinationTime = timeFormat.format(Date(firstFlight.arrivalTime)),
            duration = formatDuration(firstFlight.duration),
            airline = firstFlight.airline.name,
            travelClass = firstFlight.cabinClass.name.replace("_", " ").lowercase()
                .replaceFirstChar { it.uppercase() },
            flightType = "Direct",
            bookingId = flightBooking.bookingId,
            passengerCount = flightBooking.tickets.size,
            flightCount = flightBooking.flights.size
        )
    }

    private fun Tour.toTourUi(): TourUi = TourUi(
        id = id,
        title = title,
        imageUrl = imageUrl,
        location = location,
        rating = rating,
        price = "$ $price",
        duration = formatDuration(duration)
    )

    private fun formatDuration(duration: Duration): String = buildString {
        val hours = duration.inWholeHours
        val minutes = duration.inWholeMinutes % 60
        if (hours > 0) append("${hours}h ")
        append("${minutes}m")
    }
}
