package com.softserveacademy.home.presentation.mockdata

import com.softserveacademy.home.presentation.model.UpcomingTripUi
import com.softserveacademy.home.presentation.model.UserProfileUi

object PresentationMockData {
    val userProfile = UserProfileUi(
        name = "John Doe",
        points = 2000,
        avatarUrl = "https://i.pravatar.cc/300"
    )

    val upcomingTrip = UpcomingTripUi(
        status = "Upcoming",
        date = "Sat, Nov 23",
        originCode = "CGK",
        originTime = "09:00",
        destinationCode = "DPS",
        destinationTime = "10:30",
        duration = "1h 30m",
        airline = "Garuda Indonesia",
        travelClass = "Economy",
        flightType = "Direct",
        bookingId = "GA-880",
        passengerCount = 2,
        flightCount = 2
    )
}
