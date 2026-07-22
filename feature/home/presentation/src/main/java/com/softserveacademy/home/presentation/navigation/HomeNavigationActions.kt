package com.softserveacademy.home.presentation.navigation

import com.softserveacademy.core.domain.model.Hotel

data class HomeNavigationActions(
    val onHotelClick: (Hotel) -> Unit = {},
    val onAccountClick: () -> Unit = {},
    val onProfileClick: () -> Unit = {},
    val onJourneySeeAllClick: () -> Unit = {},
    val onHotelsSeeAllClick: () -> Unit = {},
    val onUpcomingTripClick: (String) -> Unit = {},
)
