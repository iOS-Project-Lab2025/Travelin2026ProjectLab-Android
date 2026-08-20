package com.softserveacademy.feature.favorites.tours.data.repository

import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.feature.favorites.common.domain.repository.FavoritesRepository
import com.softserveacademy.feature.favorites.tours.domain.model.FavoriteTour
import com.softserveacademy.feature.favorites.tours.domain.repository.FavoriteTourRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteTourRepositoryImpl @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : FavoriteTourRepository {

    override fun getFavoriteTours(): Flow<List<FavoriteTour>> {
        return favoritesRepository.getFavorites().map { favorites ->
            favorites.filter { it.type == "TOUR" || it.type == "TOURS" }.map { item ->
                FavoriteTour(
                    id = item.id,
                    tour = Tour(
                        id = item.id,
                        title = item.title,
                        location = item.location,
                        rating = item.rating,
                        imageList = listOf(item.imageUrl),
                    ),
                    isAvailable = true,
                    category = "Adventure"
                )
            }
        }
    }
}
