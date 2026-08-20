package com.softserveacademy.home.data.repository

import android.util.Log
import com.softserveacademy.core.domain.model.Destination
import com.softserveacademy.core.domain.model.*
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.domain.repository.PoiRepo
import com.softserveacademy.core.domain.repository.TourRepo
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.home.domain.repository.SearchFilter
import com.softserveacademy.home.domain.repository.SearchItem
import com.softserveacademy.home.domain.repository.SearchRepository
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds

private const val TAG = "SearchRepositoryImpl"

class SearchRepositoryImpl @Inject constructor(
    private val hotelRepo: HotelRepo,
    private val tourRepo: TourRepo,
    private val poiRepo: PoiRepo,
) : SearchRepository {

    override suspend fun search(
        query: String,
        filter: SearchFilter,
        location: String?,
        latitude: Double?,
        longitude: Double?,
        radius: Double?
    ): Result<List<SearchItem>> {
        // Get hotels from the API if the filter allows it
        val hotels = if (filter == SearchFilter.ALL || filter == SearchFilter.HOTELS) {
            when (val result = hotelRepo.getHotels()) {
                is AppResult.Success -> result.data.map { SearchItem.HotelItem(it) }
                is AppResult.Failure -> emptyList()
            }
        } else emptyList()

        // Get tours from the API if the filter allows it
        val tours = if (filter == SearchFilter.ALL || filter == SearchFilter.TOURS) {
            when (val result = tourRepo.getTours()) {
                is AppResult.Success -> result.data.map { SearchItem.TourItem(it) }
                is AppResult.Failure -> emptyList()
            }
        } else emptyList()

        // For destinations, we continue using mocks for now
        val otherItems = if (filter == SearchFilter.ALL || filter == SearchFilter.DESTINATIONS) {
            getMockData().filter { it is SearchItem.DestinationItem }
        } else emptyList()

        // Get POIs if the filter allows it
        val pois = if (filter == SearchFilter.ALL || filter == SearchFilter.POIS) {
            if (latitude != null && longitude != null) {
                // radius is in km, convert to meters for PoiRepo
                val radiusInMeters = radius?.let { it * 1000 }
                Log.d(TAG, "Fetching POIs: lat=$latitude, lon=$longitude, radius=$radiusInMeters")
                when (val result = poiRepo.getNearbyPlaces(latitude, longitude, radiusInMeters)) {
                    is AppResult.Success -> {
                        Log.d(TAG, "POI Repo returned ${result.data.size} items")
                        result.data.map { SearchItem.PoiItem(it) }
                    }
                    is AppResult.Failure -> {
                        Log.e(TAG, "POI Failure: ${result.error}")
                        emptyList()
                    }
                }
            } else {
                Log.d(TAG, "POI skipped: coordinates are null")
                emptyList()
            }
        } else emptyList()

        val allItems = hotels + tours + otherItems + pois

        // Filter by text
        var filtered = allItems.filter { item ->
            when (item) {
                is SearchItem.HotelItem -> item.hotel.name.contains(query, ignoreCase = true) || item.hotel.address.contains(query, ignoreCase = true)
                is SearchItem.TourItem -> item.tour.title.contains(query, ignoreCase = true) || item.tour.location.contains(query, ignoreCase = true)
                is SearchItem.DestinationItem -> item.destination.name.contains(query, ignoreCase = true) || item.destination.location.contains(query, ignoreCase = true)
                is SearchItem.PoiItem -> item.poi.name.contains(query, ignoreCase = true) || item.poi.type.contains(query, ignoreCase = true)
            }
        }

        // Filter by distance if coordinates and radius are provided
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

                    is SearchItem.PoiItem -> {
                        // POIs are already filtered by the API using circular bounds.
                        // If we have a road/walking distance, we can use it for extra precision,
                        // but if it's missing (e.g. no route found), we still keep the item.
                        val distanceKm = item.poi.distanceMeters?.toDouble()?.let { it / 1000.0 }
                        return@filter distanceKm == null || distanceKm <= radius
                    }
                }
                calculateDistance(latitude, longitude, itemLat, itemLon) <= radius
            }
        }

        // Sort POIs by ascending distance
        val finalResults = if (filter == SearchFilter.POIS) {
            filtered.filterIsInstance<SearchItem.PoiItem>()
                .sortedBy { it.poi.distanceMeters ?: Int.MAX_VALUE }
        } else if (filter == SearchFilter.ALL) {
            // In ALL, we still want POIs to be sorted among themselves if they are present?
            // Usually, we just return the mixed list. But the prompt said "found POIs should be sorted".
            // I'll sort the whole list by distance if possible.
            filtered.sortedBy { item ->
                when (item) {
                    is SearchItem.PoiItem -> item.poi.distanceMeters?.toDouble() ?: Double.MAX_VALUE
                    is SearchItem.HotelItem -> calculateDistance(latitude ?: 0.0, longitude ?: 0.0, item.hotel.latitude, item.hotel.longitude) * 1000
                    is SearchItem.TourItem -> calculateDistance(latitude ?: 0.0, longitude ?: 0.0, item.tour.latitude, item.tour.longitude) * 1000
                    is SearchItem.DestinationItem -> Double.MAX_VALUE // Mock destinations at the end
                }
            }
        } else {
            filtered
        }

        return Result.success(finalResults)
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371 // Earth radius in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return r * c
    }

    private fun getMockData(): List<SearchItem> = listOf(
        // Hotels (We only keep those that do not come from the API if necessary, but here we remove them to use only the API)

        // Tours (We only keep those that do not come from the API)

        // Destinations
        SearchItem.DestinationItem(Destination("d1", "https://picsum.photos/id/13/400/300", "Torres del Paine", "Patagonia, Chile", 4.9, 1500.0, "USD", "5D4N")),
        SearchItem.DestinationItem(Destination("d2", "https://picsum.photos/id/14/400/300", "Easter Island", "Polynesia, Chile", 4.8, 1200.0, "USD", "4D3N")),
        SearchItem.DestinationItem(Destination("d3", "https://picsum.photos/id/15/400/300", "Atacama Desert", "Antofagasta, Chile", 4.9, 800.0, "USD", "3D2N")),

        // More randoms to reach 15
        SearchItem.HotelItem(Hotel("4", "Hotel Antumalal", "Pucón, Chile", 5, 4.8, 280, listOf("https://picsum.photos/id/167/400/300"))),
        SearchItem.TourItem(Tour("t4", "City Tour Santiago", "Historic center and hills", "Santiago", listOf("https://picsum.photos/id/16/400/300"), 4.hours,
            RatePerParticipant(30.0), 4.5, TourCategory.CITY)),
        SearchItem.DestinationItem(Destination("d4", "https://picsum.photos/id/17/400/300", "Pucón", "Araucanía, Chile", 4.7, 500.0, "USD", "3D2N"))
    )
}
