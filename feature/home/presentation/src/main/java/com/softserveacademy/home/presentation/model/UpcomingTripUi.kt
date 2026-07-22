package com.softserveacademy.home.presentation.model

data class UpcomingTripUi(
    val status: String,
    val date: String,
    val originCode: String,
    val originTime: String,
    val destinationCode: String,
    val destinationTime: String,
    val duration: String,
    val airline: String,
    val travelClass: String,
    val flightType: String,
    val bookingId: String,
    val passengerCount: Int = 1,
    val flightCount: Int = 1
)