package com.softserveacademy.home.data.repository

import com.softserveacademy.core.domain.model.Destination
import com.softserveacademy.core.domain.model.*
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.domain.repository.TourRepo
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.home.domain.repository.SearchFilter
import com.softserveacademy.home.domain.repository.SearchItem
import com.softserveacademy.home.domain.repository.SearchRepository
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class SearchRepositoryImpl @Inject constructor(
    private val hotelRepo: HotelRepo,
    private val tourRepo: TourRepo,
) : SearchRepository {

    override suspend fun search(
        query: String,
        filter: SearchFilter,
        location: String?,
        latitude: Double?,
        longitude: Double?,
        radius: Double?
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

        // Para destinos, seguimos usando mocks por ahora
        val otherItems = if (filter == SearchFilter.ALL || filter == SearchFilter.DESTINATIONS) {
            getMockData().filter { it is SearchItem.DestinationItem }
        } else emptyList()

        val allItems = hotels + tours + otherItems

        // Filtramos por texto
        var filtered = allItems.filter { item ->
            when (item) {
                is SearchItem.HotelItem -> item.hotel.name.contains(query, ignoreCase = true) || item.hotel.address.contains(query, ignoreCase = true)
                is SearchItem.TourItem -> item.tour.title.contains(query, ignoreCase = true) || item.tour.location.contains(query, ignoreCase = true)
                is SearchItem.DestinationItem -> item.destination.name.contains(query, ignoreCase = true) || item.destination.location.contains(query, ignoreCase = true)
            }
        }

        // Filtramos por distancia si se proveen coordenadas y radio
        if (latitude != null && longitude != null && radius != null) {
            filtered = filtered.filter { item ->
                val itemLat: Double
                val itemLon: Double
                when (item) {
                    is SearchItem.HotelItem -> {
                        itemLat = item.hotel.latitude
                        itemLon = item.hotel.longitude
                    }
                    is SearchItem.TourItem -> {
                        itemLat = item.tour.latitude
                        itemLon = item.tour.longitude
                    }
                    is SearchItem.DestinationItem -> {
                        // Mock coordinates for destinations
                        itemLat = -33.4489 // Santiago as default mock
                        itemLon = -70.6693
                    }
                }
                calculateDistance(latitude, longitude, itemLat, itemLon) <= radius
            }
        }

        return Result.success(filtered)
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371 // Radio de la Tierra en km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return r * c
    }

    private fun getMockData(): List<SearchItem> = listOf(
        // Hotels (Solo mantenemos los que no vienen de la API si fuera necesario, pero aquí los quitamos para usar solo la API)
        
        // Tours (Solo mantenemos los que no vienen de la API)

        // Destinations
        SearchItem.DestinationItem(Destination("d1", "https://picsum.photos/id/13/400/300", "Torres del Paine", "Patagonia, Chile", 4.9, 1500.0, "USD", "5D4N")),
        SearchItem.DestinationItem(Destination("d2", "https://picsum.photos/id/14/400/300", "Easter Island", "Polynesia, Chile", 4.8, 1200.0, "USD", "4D3N")),
        SearchItem.DestinationItem(Destination("d3", "https://picsum.photos/id/15/400/300", "Atacama Desert", "Antofagasta, Chile", 4.9, 800.0, "USD", "3D2N")),

        // More randoms to reach 15
        SearchItem.DestinationItem(Destination("d4", "https://picsum.photos/id/17/400/300", "Pucón", "Araucanía, Chile", 4.7, 500.0, "USD", "3D2N"))
    )
}
