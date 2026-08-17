package com.softserveacademy.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    val ticketNumber: String,
    val passengerName: String,
    val seatNumber: String? = null,
    val gate: String? = null,
    val boardingGroup: String? = null,
    val seatClass: SeatClass = SeatClass.ECONOMY
)

@Serializable
enum class SeatClass {
    ECONOMY,
    PREMIUM_ECONOMY,
    BUSINESS,
    FIRST
}
