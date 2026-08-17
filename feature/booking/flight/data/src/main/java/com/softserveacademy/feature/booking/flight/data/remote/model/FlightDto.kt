package com.softserveacademy.feature.booking.flight.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the flight offer as received from the remote API.
 * This object is strictly for data transfer and should be mapped to a Domain Model.
 */
@Serializable
data class FlightOfferDto(
    @SerialName("id") val id: String,
    @SerialName("price") val price: Double,
    @SerialName("flight") val flight: FlightDto
)

@Serializable
data class FlightDto(
    @SerialName("id") val id: String,
    @SerialName("airline_name") val airlineName: String,
    @SerialName("airline_code") val airlineCode: String,
    @SerialName("airline_logo") val logoUrl: String,
    @SerialName("flight_number") val flightNumber: String,
    @SerialName("origin_code") val originCode: String,
    @SerialName("destination_code") val destinationCode: String,
    @SerialName("departure_time") val departureTime: Long,
    @SerialName("arrival_time") val arrivalTime: Long,
    @SerialName("cabin_class") val cabinClass: String
)