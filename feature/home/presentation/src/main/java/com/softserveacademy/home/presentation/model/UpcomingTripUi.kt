package com.softserveacademy.home.presentation.model

data class UpcomingTripUi(
    val status: String,        // "Upcoming"
    val date: String,
    val originCode: String,    // "CGK"
    val originTime: String,
    val destinationCode: String, // "DPS"
    val destinationTime: String,
    val duration: String,      // "1h 30m"
    val airline: String,
    val travelClass: String,
    val flightType: String,    // "Direct"
    val bookingId: String
)