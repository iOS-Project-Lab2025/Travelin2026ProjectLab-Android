package com.softserveacademy.core.domain.usecase.tour

import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.domain.repository.TourRepo
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for retrieving tour details.
 */
class GetTourDetailsUseCase @Inject constructor(
    private val repository: TourRepo
) {
    /**
     * Executes the tour detail retrieval.
     * @param id The unique identifier of the tour.
     * @return AppResult containing [Tour] on success.
     */
    suspend operator fun invoke(id: String): AppResult<Tour> {
        return repository.getTourById(id)
    }
}