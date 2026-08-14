package com.softserveacademy.core.domain.usecase

import com.softserveacademy.core.domain.repository.PoiRepo
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for fetching a description of the area for a location.
 */
class GetAreaDescriptionUseCase @Inject constructor(
    private val poiRepo: PoiRepo
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Result<String?> {
        return when (val result = poiRepo.getAreaDescription(latitude, longitude)) {
            is AppResult.Success -> Result.success(result.data)
            is AppResult.Failure -> Result.failure(Exception("Failed to load area description: ${result.error}"))
        }
    }
}
