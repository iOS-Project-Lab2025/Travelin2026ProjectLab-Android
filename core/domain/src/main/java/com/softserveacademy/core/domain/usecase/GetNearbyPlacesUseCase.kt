package com.softserveacademy.core.domain.usecase

import com.softserveacademy.core.domain.model.Poi
import com.softserveacademy.core.domain.repository.PoiRepo
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for fetching points of interest near a location.
 */
class GetNearbyPlacesUseCase @Inject constructor(
    private val poiRepo: PoiRepo
) {
    suspend operator fun invoke(latitude: Double, longitude: Double, requestNumber: Int): AppResult<List<Poi>> {
        return poiRepo.getNearbyPlaces(latitude, longitude, requestNumber = requestNumber)
    }
}
