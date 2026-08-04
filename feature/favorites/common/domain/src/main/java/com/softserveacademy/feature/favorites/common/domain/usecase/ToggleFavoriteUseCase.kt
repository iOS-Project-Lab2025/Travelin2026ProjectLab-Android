package com.softserveacademy.feature.favorites.common.domain.usecase

import com.softserveacademy.feature.favorites.common.domain.model.FavoriteItem
import com.softserveacademy.feature.favorites.common.domain.repository.FavoritesRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    suspend operator fun invoke(item: FavoriteItem) {
        val exists = repository.isFavorite(item.id)
        if (exists) {
            repository.removeFavorite(item.id)
        } else {
            repository.addFavorite(item)
        }
    }
}