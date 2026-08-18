package com.softserveacademy.home.presentation.navigation

data class HomeNavigationActions( 
   
    val onHotelClick: (String) -> Unit = {},
    val onTourClick: (String) -> Unit = {},
    val onPoiClick: (String) -> Unit = {},
    val onFlightsClick: () -> Unit = {},
    val onAccountClick: () -> Unit = {},
    val onProfileClick: () -> Unit = {},
    val onJourneySeeAllClick: () -> Unit = {},
    val onHotelsSeeAllClick: () -> Unit = {},
    val onUpcomingTripClick: (String) -> Unit = {},
)
