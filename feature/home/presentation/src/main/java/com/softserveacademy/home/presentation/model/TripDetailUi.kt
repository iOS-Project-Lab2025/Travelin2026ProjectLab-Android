package com.softserveacademy.home.presentation.model

import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.domain.model.FlightBooking
import com.softserveacademy.core.domain.model.Ticket
import com.softserveacademy.core.domain.model.Trip
import kotlin.time.Duration

data class TripDetailUi(
    val tripId: String,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val flightBookings: List<FlightBookingUi>,
    val hotelInfo: HotelInfoUi?,
    val tourInfo: String?
)

data class FlightBookingUi(
    val bookingId: String,
    val flights: List<FlightUi>,
    val tickets: List<TicketUi>,
    val confirmationCode: String,
    val status: String
)

data class FlightUi(
    val flightNumber: String,
    val airline: String,
    val airlineLogo: String,
    val origin: Airport,
    val destination: Airport,
    val departureTime: String,
    val arrivalTime: String,
    val duration: Duration,
    val cabinClass: String
)

data class TicketUi(
    val ticketNumber: String,
    val passengerName: String,
    val seatNumber: String?,
    val gate: String?,
    val boardingGroup: String?,
    val seatClass: String
)

data class HotelInfoUi(
    val name: String,
    val roomType: String,
    val checkIn: String,
    val checkOut: String,
    val guests: Int,
    val confirmationCode: String
)

fun Trip.toTripDetailUi(): TripDetailUi {
    val dateFormat = java.text.SimpleDateFormat("EEE, MMM d", java.util.Locale.getDefault())

    fun Duration.format(): String = buildString {
        val hours = inWholeHours
        val minutes = inWholeMinutes % 60
        if (hours > 0) append("${hours}h ")
        append("${minutes}m")
    }

    return TripDetailUi(
        tripId = id,
        destination = destination.name,
        startDate = dateFormat.format(java.util.Date(startDate)),
        endDate = dateFormat.format(java.util.Date(endDate)),
        flightBookings = flights.map { fb ->
            FlightBookingUi(
                bookingId = fb.bookingId,
                flights = fb.flights.map { f ->
                    FlightUi(
                        flightNumber = f.flightNumber,
                        airline = f.airline.name,
                        airlineLogo = f.airline.logoUrl,
                        origin = f.origin,
                        destination = f.destination,
                        departureTime = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date(f.departureTime)),
                        arrivalTime = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date(f.arrivalTime)),
                        duration = f.duration,
                        cabinClass = f.cabinClass.name.replace("_", " ").lowercase()
                            .replaceFirstChar { it.uppercase() }
                    )
                },
                tickets = fb.tickets.map { t ->
                    TicketUi(
                        ticketNumber = t.ticketNumber,
                        passengerName = t.passengerName,
                        seatNumber = t.seatNumber,
                        gate = t.gate,
                        boardingGroup = t.boardingGroup,
                        seatClass = t.seatClass.name.replace("_", " ").lowercase()
                            .replaceFirstChar { it.uppercase() }
                    )
                },
                confirmationCode = fb.confirmationCode,
                status = fb.status.name.replaceFirstChar { it.uppercase() }
            )
        },
        hotelInfo = hotel?.let { h ->
            HotelInfoUi(
                name = h.hotel.name,
                roomType = h.roomType,
                checkIn = dateFormat.format(java.util.Date(h.checkIn)),
                checkOut = dateFormat.format(java.util.Date(h.checkOut)),
                guests = h.guests,
                confirmationCode = h.confirmationCode
            )
        },
        tourInfo = tours?.firstOrNull()?.let { "${it.tour.title} - ${it.tour.location}" }
    )
}
