package com.softserveacademy.feature.booking.flight.data.remote

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import com.softserveacademy.core.domain.model.*
import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds

/**
 * Advanced Mock Implementation for Flight Data.
 * Simulates a real-world GDS (Global Distribution System) with the following features:
 * 1. Network connectivity checking.
 * 2. Realistic API latency (delay).
 * 3. Route-based filtering (Empty states for invalid routes).
 * 4. Dynamic price generation based on cabin class and index.
 *
 * @param context Application context used for checking system connectivity.
 */
class MockFlightRemoteDataSource @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: Context
) : FlightRemoteDataSource {

    /**
     * Checks if the device has an active internet connection.
     * Required to simulate technical errors (IOException).
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private val airportsMock = listOf(
        Airport("SCL", "Arturo Merino Benítez", "Santiago", "Chile"),
        Airport("LIM", "Jorge Chávez", "Lima", "Peru"),
        Airport("JFK", "John F. Kennedy", "New York", "USA"),
        Airport("MAD", "Adolfo Suárez Barajas", "Madrid", "Spain"),
        Airport("EZE", "Ezeiza", "Buenos Aires", "Argentina"),
        Airport("GRU", "Guarulhos", "Sao Paulo", "Brazil"),
        Airport("SAN", "San Diego", "San Diego, California", "USA"),
        Airport("SAP", "San Pedro Sula", "San Pedro", "Honduras")
    )

    private val airlinesMock = listOf(
        Airline("LA", "Latam Airlines", "https://images.kiwi.com/airlines/64/LA.png"),
        Airline("H2", "Sky Airline", "https://images.kiwi.com/airlines/64/H2.png"),
        Airline("JA", "JetSmart", "https://images.kiwi.com/airlines/64/JA.png"),
        Airline("AR", "Aerolineas Argentinas", "https://images.kiwi.com/airlines/64/AR.png"),
        Airline("AV", "Avianca", "https://images.kiwi.com/airlines/64/AV.png"),
        Airline("CM", "Copa Airlines", "https://images.kiwi.com/airlines/64/CM.png")
    )

    /**
     * Simulates fetching flights with business logic for routes and pricing.
     * Throws [IOException] if network is unavailable.
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun getFlightOffers(
        origin: String,
        destination: String,
        passengerCounts: Map<PassengerType, Int>,
        cabinClass: CabinClass,
        departureDate: Long?,
        returnDate: Long?
    ): List<FlightOffer> {
        if (!isNetworkAvailable()) throw java.io.IOException()

        // Simulate network round-trip time
        delay(1500.milliseconds)

        val now = System.currentTimeMillis()

        // Realistic route mapping to test Empty States
        val routes = mapOf(
            "SCL" to listOf("LIM", "JFK", "MAD", "EZE"),
            "LIM" to listOf("SCL", "JFK", "GRU"),
            "JFK" to listOf("SCL", "LIM", "MAD")
        )

        val destinationExists = routes[origin]?.contains(destination) ?: false
        if (!destinationExists) return emptyList()

        val offerCount = when(cabinClass) {
            CabinClass.ECONOMY -> 6
            CabinClass.BUSINESS -> 2
            CabinClass.FIRST -> 1
            CabinClass.PREMIUM_ECONOMY -> 3
        }

        return airlinesMock.take(offerCount).mapIndexed { index, airline ->
            val flightId = "FL-${airline.code}-${100 + index}"
            FlightOffer(
                id = "OFFER-$flightId",
                basePrice = when(cabinClass) {
                    CabinClass.ECONOMY -> 450.0 + (index * 50)
                    CabinClass.BUSINESS -> 1200.0 + (index * 150)
                    CabinClass.FIRST -> 3500.0
                    else -> 600.0
                },
                flight = Flight(
                    id = flightId,
                    airline = airline,
                    flightNumber = "${airline.code}${300 + index}",
                    origin = airportsMock.find { it.code == origin } ?: Airport(origin, "Departure", "City", "Country"),
                    destination = airportsMock.find { it.code == destination } ?: Airport(destination, "Arrival", "City", "Country"),
                    departureTime = departureDate ?: (now + (index + 2).hours.inWholeMilliseconds),
                    arrivalTime = (departureDate ?: now) + (index + 5).hours.inWholeMilliseconds,
                    duration = 3.hours,
                    cabinClass = cabinClass
                )
            )
        }
    }

    /**
     * Simulates remote airport search with network validation.
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun searchAirports(query: String): List<Airport> {
        if (!isNetworkAvailable()) throw java.io.IOException()

        if (query.length < 2) return emptyList()
        return airportsMock.filter {
            it.code.contains(query, ignoreCase = true) || it.city.contains(query, ignoreCase = true)
        }
    }
}