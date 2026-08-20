package com.softserveacademy.feature.favorites.common.domain.usecase

import com.softserveacademy.feature.favorites.common.domain.model.FavoriteItem
import com.softserveacademy.feature.favorites.common.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    operator fun invoke(): Flow<List<FavoriteItem>> {
        return repository.getFavorites()
    }
}