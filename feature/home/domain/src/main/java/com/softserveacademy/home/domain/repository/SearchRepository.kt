package com.softserveacademy.home.domain.repository

import com.softserveacademy.core.domain.model.Destination
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.Poi
import com.softserveacademy.core.domain.model.Tour

/**
 * Interface for the search repository.
 * Handles queries for different types of travel items.
 */
interface SearchRepository {
    suspend fun search(
        query: String,
        filter: SearchFilter,
        location: String? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        radius: Double? = null
    ): Result<List<SearchItem>>
}

/**
 * Filter categories for the search.
 */
enum class SearchFilter { ALL, HOTELS, TOURS, DESTINATIONS, POIS }

/**
 * A sealed class to unify different types of results for the UI.
 */
sealed class SearchItem {
    data class HotelItem(val hotel: Hotel) : SearchItem()
    data class TourItem(val tour: Tour) : SearchItem()
    data class DestinationItem(val destination: Destination) : SearchItem()
    data class PoiItem(val poi: Poi) : SearchItem()
}