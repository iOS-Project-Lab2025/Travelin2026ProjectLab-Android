package com.softserveacademy.feature.favorites.tours.domain.repository

import com.softserveacademy.feature.favorites.tours.domain.model.FavoriteTour
import kotlinx.coroutines.flow.Flow

interface FavoriteTourRepository {
    fun getFavoriteTours(): Flow<List<FavoriteTour>>
}
