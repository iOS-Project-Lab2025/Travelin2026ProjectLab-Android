package com.softserveacademy.home.presentation.state

import com.softserveacademy.core.domain.model.Destination
import com.softserveacademy.core.domain.model.HomeHotel
import com.softserveacademy.core.domain.model.UpcomingTrip
import com.softserveacademy.core.domain.model.UserProfile


data class HomeUiState(
    val userProfile: SectionState<UserProfile> = SectionState.Loading,
    val upcomingTrip: SectionState<UpcomingTrip> = SectionState.Loading,
    val journeyTogether: SectionState<List<Destination>> = SectionState.Loading,
    val hotelsRecommended: SectionState<List<HomeHotel>> = SectionState.Loading,
    val searchQuery: String = ""
)