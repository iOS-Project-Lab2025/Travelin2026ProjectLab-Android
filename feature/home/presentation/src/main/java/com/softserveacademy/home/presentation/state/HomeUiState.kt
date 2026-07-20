package com.softserveacademy.home.presentation.state

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.home.presentation.model.DestinationUi
import com.softserveacademy.home.presentation.model.UpcomingTripUi
import com.softserveacademy.home.presentation.model.UserProfileUi


data class HomeUiState(
    val userProfile: SectionState<UserProfileUi> = SectionState.Loading,
    val upcomingTrip: SectionState<UpcomingTripUi> = SectionState.Loading,
    val journeyTogether: SectionState<List<DestinationUi>> = SectionState.Loading,
    val hotelsRecommended: SectionState<List<Hotel>> = SectionState.Loading,
    val searchQuery: String = ""
)