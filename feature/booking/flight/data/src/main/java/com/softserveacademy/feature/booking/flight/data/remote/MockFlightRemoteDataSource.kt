package com.softserveacademy.feature.booking.flight.data.remote

import android.Manifest
import android.R
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import com.softserveacademy.core.domain.model.*
import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.milliseconds


/**
 * Professional Mock Implementation for Flight Data.
 * Simulates a real-world GDS (Global Distribution System) response with multiple carriers.
 */
class MockFlightRemoteDataSource @Inject constructor(@dagger.hilt.android.qualifiers.ApplicationContext private val context: android.content.Context) : FlightRemoteDataSource {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    // 1. Database of realistic airports for the demo
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

    // 2. Database of real airlines with branding
    private val airlinesMock = listOf(
        Airline("LA", "Latam Airlines", "https://images.kiwi.com/airlines/64/LA.png"),
        Airline("H2", "Sky Airline", "https://images.kiwi.com/airlines/64/H2.png"),
        Airline("JA", "JetSmart", "https://images.kiwi.com/airlines/64/JA.png"),
        Airline("AR", "Aerolineas Argentinas", "https://images.kiwi.com/airlines/64/AR.png"),
        Airline("AV", "Avianca", "https://images.kiwi.com/airlines/64/AV.png"),
        Airline("CM", "Copa Airlines", "https://images.kiwi.com/airlines/64/CM.png")
    )

    /**
     * Generates a list of flight offers based on the search criteria.
     * Prices and times are generated dynamically for realism.
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
        if (!isNetworkAvailable())  {
            throw IOException()
        }
        // Simulate network latency (consistent with real APIs)
        delay(1500.milliseconds)

        val now = System.currentTimeMillis()

        // 1. LÓGICA DE RUTAS REALISTA (Para el Empty State)
        val routes = mapOf(
            "SCL" to listOf("LIM", "JFK", "MAD", "EZE"),
            "LIM" to listOf("SCL", "JFK", "GRU"),
            "JFK" to listOf("SCL", "LIM", "MAD")
        )

        val destinationExists = routes[origin]?.contains(destination) ?: false
        if (!destinationExists) return emptyList() // <--- DISPARA EL EMPTY STATE

        // 1. DETERMINE THE COUNT BASED ON CABIN
        val offerCount = when(cabinClass) {
            CabinClass.ECONOMY -> 6
            CabinClass.BUSINESS -> 2
            CabinClass.FIRST -> 1
            CabinClass.PREMIUM_ECONOMY -> 3 // Default for Premium
        }

        // 2. GENERATE AND TAKE N DIFFERENT AIRLINES
        return airlinesMock.take(offerCount).mapIndexed { index, airline ->
            val flightId = "FL-${airline.code}-${100 + index}"
            FlightOffer(
                id = "OFFER-$flightId",
                basePrice = when(cabinClass) {
                    CabinClass.ECONOMY -> 450000.0 + (index * 5000)
                    CabinClass.BUSINESS -> 1200000.0 + (index * 15000)
                    CabinClass.FIRST -> 3500000.0
                    else -> 600000.0
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
                    cabinClass = cabinClass // <--- ALWAYS MATCHES YOUR SEARCH
                )
            )
        }
    }

    /**
     * Search airports logic for the autocomplete feature.
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun searchAirports(query: String): List<Airport> {

        if (!isNetworkAvailable()) {
            throw IOException()
        }
        if (query.length < 2) return emptyList()
        return airportsMock.filter {
            it.code.contains(query, ignoreCase = true) ||
                    it.city.contains(query, ignoreCase = true)
        }
    }
}