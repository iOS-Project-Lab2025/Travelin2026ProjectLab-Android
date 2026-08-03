package com.softserveacademy.core.data.repository

import com.softserveacademy.core.data.api.TourApiService
import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.domain.repository.TourRepo
import com.softserveacademy.core.error.mapper.ExceptionMapper
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.core.error.util.safeCall
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of the [TourRepo] interface that fetches data from a real API.
 */
@Singleton
class TourRepoImpl @Inject constructor(
    private val tourApiService: TourApiService,
    private val mapper: ExceptionMapper
) : TourRepo {

    override suspend fun getTours(): AppResult<List<Tour>> = safeCall(mapper) {
        tourApiService.getTours()
    }

    override suspend fun getTourById(id: String): AppResult<Tour> = safeCall(mapper) {
        tourApiService.getTourById(id)
    }
}
