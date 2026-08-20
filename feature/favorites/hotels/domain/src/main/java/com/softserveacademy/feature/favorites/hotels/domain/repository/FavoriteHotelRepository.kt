package com.softserveacademy.feature.favorites.hotels.domain.repository
import com.softserveacademy.feature.favorites.hotels.domain.model.FavoriteHotel
import kotlinx.coroutines.flow.Flow

interface FavoriteHotelRepository {
    fun getFavoriteHotels(): Flow<List<FavoriteHotel>>
}