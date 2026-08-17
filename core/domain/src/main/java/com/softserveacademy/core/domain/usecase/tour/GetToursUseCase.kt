package com.softserveacademy.core.domain.usecase.tour

import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.domain.repository.TourRepo
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for retrieving all available tours.
 */
class GetToursUseCase @Inject constructor(
    private val repository: TourRepo,
) {
    suspend operator fun invoke(): AppResult<List<Tour>> {
        return repository.getTours()
    }
}
