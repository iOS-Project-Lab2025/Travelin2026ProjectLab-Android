package com.softserveacademy.feature.booking.flight.data.mapper

import com.softserveacademy.core.domain.model.*
import com.softserveacademy.feature.booking.flight.data.remote.model.*
import kotlin.time.Duration.Companion.milliseconds

/**
 * Extension function to convert a Data Transfer Object (DTO) into a clean Domain Model.
 * This keeps the UI logic independent from the API structure.
 */
fun FlightOfferDto.toDomain(): FlightOffer {
    return FlightOffer(
        id = this.id,
        basePrice = this.price,
        flight = this.flight.toDomain()
    )
}

private fun FlightDto.toDomain(): Flight {
    return Flight(
        id = this.id,
        airline = Airline(code = this.airlineCode, name = this.airlineName, logoUrl = this.logoUrl),
        flightNumber = this.flightNumber,
        origin = Airport(code = this.originCode, name = "", city = "", country = ""), // Detailed info comes from search
        destination = Airport(code = this.destinationCode, name = "", city = "", country = ""),
        departureTime = this.departureTime,
        arrivalTime = this.arrivalTime,
        duration = (this.arrivalTime - this.departureTime).milliseconds,
        cabinClass = CabinClass.valueOf(this.cabinClass.uppercase())
    )
}