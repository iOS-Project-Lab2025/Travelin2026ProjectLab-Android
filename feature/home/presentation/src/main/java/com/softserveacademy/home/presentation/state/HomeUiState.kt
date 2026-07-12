package com.softserveacademy.home.presentation.state

import com.softserveacademy.home.domain.model.Destination
import com.softserveacademy.home.domain.model.Hotel
import com.softserveacademy.home.domain.model.UpcomingTrip
import com.softserveacademy.home.domain.model.UserProfile


data class HomeUiState(
    val userProfile: SectionState<UserProfile> = SectionState.Loading,
    val upcomingTrip: SectionState<UpcomingTrip> = SectionState.Loading,
    val journeyTogether: SectionState<List<Destination>> = SectionState.Loading,
    val hotelsRecommended: SectionState<List<Hotel>> = SectionState.Loading,
    val searchQuery: String = ""
)