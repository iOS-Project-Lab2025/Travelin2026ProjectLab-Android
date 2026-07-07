package com.softserveacademy.core.domain.model

data class UpcomingTrip(
    val status: String,
    val date: String,
    val originCode: String,
    val originTime: String,
    val destinationCode: String,
    val destinationTime: String,
    val durationMinutes: Int,
    val airline: String,
    val travelClass: String,
    val flightType: String,
    val bookingId: String
)