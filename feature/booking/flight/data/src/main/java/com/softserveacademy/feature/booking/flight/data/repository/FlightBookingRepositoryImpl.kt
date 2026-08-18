package com.softserveacademy.feature.booking.flight.data.repository

import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.model.FlightBooking
import com.softserveacademy.core.domain.model.Ticket
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingRepository
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * Data layer implementation for managing finalized flight bookings.
 *
 * Handles the persistence of official booking records. Currently simulates
 * network latency and successful storage.
 */
class FlightBookingRepositoryImpl @Inject constructor() : FlightBookingRepository {

    override suspend fun saveBooking(booking: FlightBooking): AppResult<FlightBooking> {
        delay(1500.milliseconds) // We simulate a network delay

        // Simulation: Server generates official e-tickets
        val issuedTickets = booking.passengers.flatMap { pax ->
            booking.flights.map { flight ->
                Ticket(
                    ticketNumber = "TK-${(100000..999999).random()}",
                    passengerName = "${pax.firstName} ${pax.lastName}",
                    flightId = flight.id,
                    flightNumber = flight.flightNumber,
                    originCode = flight.origin.code,
                    destinationCode = flight.destination.code,
                    seatNumber = "${(1..30).random()}${('A'..'F').random()}",
                    gate = "${('A'..'C').random()}${(1..20).random()}",
                    boardingGroup = "Group ${(1..4).random()}",
                    cabinClass = flight.cabinClass
                )
            }
        }

        // return the object enriched with tickets by API
        val confirmedBooking = booking.copy(
            tickets = issuedTickets,
            status = BookingStatus.COMPLETED
        )

        return AppResult.Success(confirmedBooking)
    }
}
