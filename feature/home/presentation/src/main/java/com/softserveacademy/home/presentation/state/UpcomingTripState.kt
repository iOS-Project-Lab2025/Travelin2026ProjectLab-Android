package com.softserveacademy.home.presentation.state

import com.softserveacademy.core.domain.model.Trip

data class UpcomingTripState(
    val trip: SectionState<Trip> = SectionState.Loading
)
