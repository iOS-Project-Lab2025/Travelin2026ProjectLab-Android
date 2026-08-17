package com.softserveacademy.feature.favorites.hotels.domain.usecase

import com.softserveacademy.feature.favorites.hotels.domain.model.FavoriteHotel
import com.softserveacademy.feature.favorites.hotels.domain.repository.FavoriteHotelRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteHotelsUseCase @Inject constructor(
    private val repository: FavoriteHotelRepository
) {
    operator fun invoke(): Flow<List<FavoriteHotel>> {
        return repository.getFavoriteHotels()
    }
}
