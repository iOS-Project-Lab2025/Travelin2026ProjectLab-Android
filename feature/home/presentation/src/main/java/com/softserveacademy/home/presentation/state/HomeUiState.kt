package com.softserveacademy.home.presentation.state

import com.softserveacademy.core.presentation.

data class HomeUiState(
    val userProfile: SectionState<UserProfileUi> = SectionState.Loading,
    val upcomingTrip: SectionState<UpcomingTripUi> = SectionState.Loading,
    val journeyTogether: SectionState<List<DestinationUi>> = SectionState.Loading,
    val hotelsRecommended: SectionState<List<HotelUi>> = SectionState.Loading,
    val searchQuery: String = ""
)