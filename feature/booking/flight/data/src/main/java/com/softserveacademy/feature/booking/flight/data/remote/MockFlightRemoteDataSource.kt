package com.softserveacademy.feature.booking.flight.data.remote

import com.softserveacademy.core.domain.model.*
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.milliseconds

/**
 * Professional Mock Implementation for Flight Data.
 * Simulates a real-world GDS (Global Distribution System) response with multiple carriers.
 */
class MockFlightRemoteDataSource @Inject constructor() : FlightRemoteDataSource {

    // 1. Database of realistic airports for the demo
    private val airportsMock = listOf(
        Airport("SCL", "Arturo Merino Benítez", "Santiago", "Chile"),
        Airport("LIM", "Jorge Chávez", "Lima", "Peru"),
        Airport("JFK", "John F. Kennedy", "New York", "USA"),
        Airport("MAD", "Adolfo Suárez Barajas", "Madrid", "Spain"),
        Airport("EZE", "Ezeiza", "Buenos Aires", "Argentina"),
        Airport("GRU", "Guarulhos", "Sao Paulo", "Brazil")
    )

    // 2. Database of real airlines with branding
    private val airlinesMock = listOf(
        Airline("LA", "Latam Airlines", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/LATAM_Logo.svg/512px-LATAM_Logo.svg.png"),
        Airline("H2", "Sky Airline", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/Sky_Airline_logo.svg/512px-Sky_Airline_logo.svg.png"),
        Airline("JA", "JetSmart", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/JetSmart_logo.svg/512px-JetSmart_logo.svg.png"),
        Airline("AR", "Aerolineas Argentinas", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/60/Logo_de_Aerol%C3%ADneas_Argentinas.svg/512px-Logo_de_Aerol%C3%ADneas_Argentinas.svg.png"),
        Airline("AV", "Avianca", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Avianca_Logo.svg/512px-Avianca_Logo.svg.png")
    )

    /**
     * Generates a list of flight offers based on the search criteria.
     * Prices and times are generated dynamically for realism.
     */
    override suspend fun getFlightOffers(
        origin: String,
        destination: String,
        passengerCounts: Map<PassengerType, Int>
    ): List<FlightOffer> {
        // Simulate network latency (consistent with real APIs)
        delay(1500.milliseconds)

        val now = System.currentTimeMillis()

        // Resolve airport details from our mock DB or create a fallback
        val originAirport = airportsMock.find { it.code == origin }
            ?: Airport(origin, "Departure Airport", "Origin City", "Country")
        val destAirport = airportsMock.find { it.code == destination }
            ?: Airport(destination, "Arrival Airport", "Dest City", "Country")

        // Create an offer for each airline in our fleet
        return airlinesMock.mapIndexed { index, airline ->
            val flightId = "FL-${airline.code}-${100 + index}"
            val basePrice = when (airline.code) {
                "LA" -> 450000.0
                "H2" -> 280000.0
                "JA" -> 150000.0
                else -> 320000.0
            }

            FlightOffer(
                id = "OFFER-$flightId",
                flight = Flight(
                    id = flightId,
                    airline = airline,
                    flightNumber = "${airline.code}${300 + index}",
                    origin = originAirport,
                    destination = destAirport,
                    // Staggered times for visual variety
                    departureTime = now + (index + 2).hours.inWholeMilliseconds,
                    arrivalTime = now + (index + 4).hours.inWholeMilliseconds + 45.minutes.inWholeMilliseconds,
                    duration = 2.hours + 45.minutes,
                    cabinClass = CabinClass.ECONOMY
                ),
                basePrice = basePrice + (index * 1500) // Slight variations
            )
        }
    }

    /**
     * Search airports logic for the autocomplete feature.
     */
    override suspend fun searchAirports(query: String): List<Airport> {
        if (query.length < 2) return emptyList()
        return airportsMock.filter {
            it.code.contains(query, ignoreCase = true) ||
                    it.city.contains(query, ignoreCase = true)
        }
    }
}