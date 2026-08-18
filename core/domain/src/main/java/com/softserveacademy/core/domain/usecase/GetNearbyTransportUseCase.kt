package com.softserveacademy.core.domain.usecase

import com.softserveacademy.core.domain.model.Poi
import com.softserveacademy.core.domain.repository.PoiRepo
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for fetching nearby transport hubs for a location.
 */
class GetNearbyTransportUseCase @Inject constructor(
    private val poiRepo: PoiRepo
) {
    suspend operator fun invoke(latitude: Double, longitude: Double, requestNumber: Int): AppResult<List<Poi>> {
        return poiRepo.getNearbyTransport(latitude, longitude, requestNumber)
    }
}
