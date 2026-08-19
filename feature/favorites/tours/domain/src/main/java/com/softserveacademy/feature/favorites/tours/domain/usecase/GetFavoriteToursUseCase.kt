package com.softserveacademy.feature.favorites.tours.domain.usecase

import com.softserveacademy.feature.favorites.tours.domain.model.FavoriteTour
import com.softserveacademy.feature.favorites.tours.domain.repository.FavoriteTourRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteToursUseCase @Inject constructor(
    private val repository: FavoriteTourRepository
) {
    operator fun invoke(): Flow<List<FavoriteTour>> {
        return repository.getFavoriteTours()
    }
}
