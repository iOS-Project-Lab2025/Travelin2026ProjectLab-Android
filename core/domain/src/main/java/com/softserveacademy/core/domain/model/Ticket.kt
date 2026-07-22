package com.softserveacademy.core.domain.model

data class Ticket(
    val ticketNumber: String,
    val passengerName: String,
    val seatNumber: String? = null,
    val gate: String? = null,
    val boardingGroup: String? = null,
    val seatClass: SeatClass = SeatClass.ECONOMY
)

enum class SeatClass {
    ECONOMY,
    PREMIUM_ECONOMY,
    BUSINESS,
    FIRST
}
