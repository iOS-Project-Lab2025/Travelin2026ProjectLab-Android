package com.softserveacademy.home.presentation.state

import com.softserveacademy.home.presentation.model.DestinationUi
import com.softserveacademy.home.presentation.model.HotelUi
import com.softserveacademy.home.presentation.model.UpcomingTripUi
import com.softserveacademy.home.presentation.model.UserProfileUi

data class HomeUiState(
    val userProfile: SectionState<UserProfileUi> = SectionState.Loading,
    val upcomingTrip: SectionState<UpcomingTripUi> = SectionState.Loading,
    val journeyTogether: SectionState<List<DestinationUi>> = SectionState.Loading,
    val hotelsRecommended: SectionState<List<HotelUi>> = SectionState.Loading,
    val searchQuery: String = ""
)