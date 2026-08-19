package com.softserveacademy.feature.favorites.hotels.data.repository

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.HotelRoom
import com.softserveacademy.feature.favorites.common.domain.repository.FavoritesRepository
import com.softserveacademy.feature.favorites.hotels.domain.model.FavoriteHotel
import com.softserveacademy.feature.favorites.hotels.domain.repository.FavoriteHotelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteHotelRepositoryImpl @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : FavoriteHotelRepository {

    override fun getFavoriteHotels(): Flow<List<FavoriteHotel>> {
        return favoritesRepository.getFavorites().map { favorites ->
            favorites.filter { it.type == "HOTEL" }.map { item ->
                FavoriteHotel(
                    id = item.id,
                    hotel = Hotel(
                        id = item.id,
                        name = item.title,
                        address = item.location,
                        reviewRating = item.rating,
                        imageList = listOf(item.imageUrl),
                        pricePerNight = item.price ?: 0.0,
                        rooms = listOf(
                            HotelRoom(
                                type = "Standard",
                                description = "Comfortable room",
                                maxOccupancy = 2,
                                bedType = "1 Bed",
                                amenities = emptyList(),
                                pricePerNight = item.price ?: 0.0
                            )
                        ),
                    ),
                    isAvailable = true,
                    roomType = "1 Bed" // Mapping default values as per item structure
                )
            }
        }
    }
}
