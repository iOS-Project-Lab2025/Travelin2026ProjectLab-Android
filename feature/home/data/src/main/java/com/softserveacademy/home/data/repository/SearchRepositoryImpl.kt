package com.softserveacademy.home.data.repository

import com.softserveacademy.core.domain.model.*
import com.softserveacademy.home.domain.repository.SearchFilter
import com.softserveacademy.home.domain.repository.SearchItem
import com.softserveacademy.home.domain.repository.SearchRepository
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class SearchRepositoryImpl @Inject constructor() : SearchRepository {

    override suspend fun search(
        query: String,
        filter: SearchFilter,
        location: String?
    ): Result<List<SearchItem>> {
        // Simulamos un delay de red
        kotlinx.coroutines.delay(1000)

        val allItems = getMockData()

        // Filtramos por texto y por categoría
        val filtered = allItems.filter { item ->
            val matchesQuery = when (item) {
                is SearchItem.HotelItem -> item.hotel.name.contains(query, ignoreCase = true) || item.hotel.address.contains(query, ignoreCase = true)
                is SearchItem.FlightItem -> item.flight.destination.city.contains(query, ignoreCase = true) || item.flight.airline.name.contains(query, ignoreCase = true)
                is SearchItem.TourItem -> item.tour.title.contains(query, ignoreCase = true) || item.tour.location.contains(query, ignoreCase = true)
                is SearchItem.DestinationItem -> item.destination.name.contains(query, ignoreCase = true) || item.destination.location.contains(query, ignoreCase = true)
            }

            val matchesFilter = when (filter) {
                SearchFilter.ALL -> true
                SearchFilter.HOTELS -> item is SearchItem.HotelItem
                SearchFilter.FLIGHTS -> item is SearchItem.FlightItem
                SearchFilter.TOURS -> item is SearchItem.TourItem
                SearchFilter.DESTINATIONS -> item is SearchItem.DestinationItem
            }

            matchesQuery && matchesFilter
        }

        return Result.success(filtered)
    }

    private fun getMockData(): List<SearchItem> = listOf(
        // Hotels
        SearchItem.HotelItem(Hotel(1, "San Alfonso del Mar", "Algarrobo, Chile", 5, 4.8, 200, listOf("https://picsum.photos/id/164/400/300"))),
        SearchItem.HotelItem(Hotel(2, "Icon Hotel", "Santiago, Chile", 4, 4.5, 120, listOf("https://picsum.photos/id/165/400/300"))),
        SearchItem.HotelItem(Hotel(3, "The Ritz-Carlton", "Santiago, Chile", 5, 4.9, 350, listOf("https://picsum.photos/id/166/400/300"))),

        // Tours
        SearchItem.TourItem(Tour("t1", "Cajón del Maipo Trekking", "Full day trekking in the Andes", "San José de Maipo", "https://picsum.photos/id/10/400/300", 8.hours, 45.0, 4.8f, TourCategory.ADVENTURE)),
        SearchItem.TourItem(Tour("t2", "Wine Tasting Tour", "Visit 3 premium vineyards", "Valle de Casablanca", "https://picsum.photos/id/11/400/300", 6.hours, 65.0, 4.9f, TourCategory.GASTRONOMY)),
        SearchItem.TourItem(Tour("t3", "Valparaiso Walking Tour", "Graffiti and elevators", "Valparaíso", "https://picsum.photos/id/12/400/300", 3.hours, 25.0, 4.7f, TourCategory.CULTURE)),

        // Destinations
        SearchItem.DestinationItem(Destination("d1", "https://picsum.photos/id/13/400/300", "Torres del Paine", "Patagonia, Chile", 4.9, 1500.0, "USD", "5D4N")),
        SearchItem.DestinationItem(Destination("d2", "https://picsum.photos/id/14/400/300", "Easter Island", "Polynesia, Chile", 4.8, 1200.0, "USD", "4D3N")),
        SearchItem.DestinationItem(Destination("d3", "https://picsum.photos/id/15/400/300", "Atacama Desert", "Antofagasta, Chile", 4.9, 800.0, "USD", "3D2N")),

        // Flights (Mocking simpler data for preview)
        SearchItem.FlightItem(Flight("f1", Airline("LA", "LATAM", ""), "LA202", Airport("SCL", "Santiago", "Santiago", "Chile"), Airport("JFK", "John F. Kennedy", "New York", "USA"), 0, 0, 10.hours, CabinClass.ECONOMY)),
        SearchItem.FlightItem(Flight("f2", Airline("H2", "SKY", ""), "H2101", Airport("SCL", "Santiago", "Santiago", "Chile"), Airport("LIM", "Jorge Chavez", "Lima", "Peru"), 0, 0, 3.hours, CabinClass.ECONOMY)),

        // More randoms to reach 15
        SearchItem.HotelItem(Hotel(4, "Hotel Antumalal", "Pucón, Chile", 5, 4.8, 280, listOf("https://picsum.photos/id/167/400/300"))),
        SearchItem.TourItem(Tour("t4", "City Tour Santiago", "Historic center and hills", "Santiago", "https://picsum.photos/id/16/400/300", 4.hours, 30.0, 4.5f, TourCategory.CITY)),
        SearchItem.DestinationItem(Destination("d4", "https://picsum.photos/id/17/400/300", "Pucón", "Araucanía, Chile", 4.7, 500.0, "USD", "3D2N"))
    )
}