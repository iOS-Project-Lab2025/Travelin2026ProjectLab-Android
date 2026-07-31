package com.softserveacademy.home.presentation.navigation

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.home.presentation.model.TourUi

data class HomeNavigationActions(
    val onHotelClick: (Hotel) -> Unit = {},
    val onTourClick: (TourUi) -> Unit = {},
    val onAccountClick: () -> Unit = {},
    val onProfileClick: () -> Unit = {},
    val onJourneySeeAllClick: () -> Unit = {},
    val onHotelsSeeAllClick: () -> Unit = {},
    val onUpcomingTripClick: (String) -> Unit = {},
)
