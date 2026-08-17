package com.softserveacademy.home.data.repository

import com.softserveacademy.core.domain.model.Destination
import com.softserveacademy.core.domain.model.Flight
import com.softserveacademy.core.domain.model.Airline
import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.domain.model.CabinClass
import com.softserveacademy.core.domain.model.*
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.domain.repository.TourRepo
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.home.domain.repository.SearchFilter
import com.softserveacademy.home.domain.repository.SearchItem
import com.softserveacademy.home.domain.repository.SearchRepository
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds

class SearchRepositoryImpl @Inject constructor(
    private val hotelRepo: HotelRepo,
    private val tourRepo: TourRepo,
) : SearchRepository {

    override suspend fun search(
        query: String,
        filter: SearchFilter,
        location: String?
    ): Result<List<SearchItem>> {
        // Obtenemos hoteles de la API si el filtro lo permite
        val hotels = if (filter == SearchFilter.ALL || filter == SearchFilter.HOTELS) {
            when (val result = hotelRepo.getHotels()) {
                is AppResult.Success -> result.data.map { SearchItem.HotelItem(it) }
                is AppResult.Failure -> emptyList()
            }
        } else emptyList()

        // Obtenemos tours de la API si el filtro lo permite
        val tours = if (filter == SearchFilter.ALL || filter == SearchFilter.TOURS) {
            when (val result = tourRepo.getTours()) {
                is AppResult.Success -> result.data.map { SearchItem.TourItem(it) }
                is AppResult.Failure -> emptyList()
            }
        } else emptyList()

        // Para vuelos y destinos, seguimos usando mocks por ahora
        val otherItems = if (filter == SearchFilter.ALL || filter == SearchFilter.FLIGHTS || filter == SearchFilter.DESTINATIONS) {
            getMockData().filter { 
                (it is SearchItem.FlightItem && (filter == SearchFilter.ALL || filter == SearchFilter.FLIGHTS)) ||
                (it is SearchItem.DestinationItem && (filter == SearchFilter.ALL || filter == SearchFilter.DESTINATIONS))
            }
        } else emptyList()

        val allItems = hotels + tours + otherItems

        // Filtramos por texto
        val filtered = allItems.filter { item ->
            when (item) {
                is SearchItem.HotelItem -> item.hotel.name.contains(query, ignoreCase = true) || item.hotel.address.contains(query, ignoreCase = true)
                is SearchItem.FlightItem -> item.flight.destination.city.contains(query, ignoreCase = true) || item.flight.airline.name.contains(query, ignoreCase = true)
                is SearchItem.TourItem -> item.tour.title.contains(query, ignoreCase = true) || item.tour.location.contains(query, ignoreCase = true)
                is SearchItem.DestinationItem -> item.destination.name.contains(query, ignoreCase = true) || item.destination.location.contains(query, ignoreCase = true)
            }
        }

        return Result.success(filtered)
    }

    private fun getMockData(): List<SearchItem> = listOf(
        // Hotels (Solo mantenemos los que no vienen de la API si fuera necesario, pero aquí los quitamos para usar solo la API)
        
        // Tours (Solo mantenemos los que no vienen de la API)

        // Destinations
        SearchItem.DestinationItem(Destination("d1", "https://picsum.photos/id/13/400/300", "Torres del Paine", "Patagonia, Chile", 4.9, 1500.0, "USD", "5D4N")),
        SearchItem.DestinationItem(Destination("d2", "https://picsum.photos/id/14/400/300", "Easter Island", "Polynesia, Chile", 4.8, 1200.0, "USD", "4D3N")),
        SearchItem.DestinationItem(Destination("d3", "https://picsum.photos/id/15/400/300", "Atacama Desert", "Antofagasta, Chile", 4.9, 800.0, "USD", "3D2N")),

        // Flights (Mocking simpler data for preview)
        SearchItem.FlightItem(Flight("f1", Airline("LA", "LATAM", ""), "LA202", Airport("SCL", "Santiago", "Santiago", "Chile"), Airport("JFK", "John F. Kennedy", "New York", "USA"), 0, 0, 10.hours, CabinClass.ECONOMY)),
        SearchItem.FlightItem(Flight("f2", Airline("H2", "SKY", ""), "H2101", Airport("SCL", "Santiago", "Santiago", "Chile"), Airport("LIM", "Jorge Chavez", "Lima", "Peru"), 0, 0, 3.hours, CabinClass.ECONOMY)),

        // More randoms to reach 15
        SearchItem.DestinationItem(Destination("d4", "https://picsum.photos/id/17/400/300", "Pucón", "Araucanía, Chile", 4.7, 500.0, "USD", "3D2N"))
    )
}
