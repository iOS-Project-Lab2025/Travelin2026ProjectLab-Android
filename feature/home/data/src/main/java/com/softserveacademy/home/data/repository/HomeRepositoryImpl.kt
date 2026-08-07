package com.softserveacademy.home.data.repository

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.domain.model.Trip
import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.domain.repository.TourRepo
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.home.data.mockdata.HomeMockData
import com.softserveacademy.home.domain.repository.HomeRepository
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val hotelRepo: HotelRepo,
    private val tourRepo: TourRepo
) : HomeRepository {

    override suspend fun getUserProfile(): Result<UserProfile> {
        delay(500.milliseconds)
        return Result.success(HomeMockData.user)
    }

    override suspend fun getUpcomingTrip(): Result<Trip?> {
        delay(700.milliseconds)
        return Result.success(HomeMockData.trip)
    }

    override suspend fun getJourneyTogether(): Result<List<Tour>> {
        delay(700.milliseconds)
        return when (val result = tourRepo.getTours()) {
            is AppResult.Success -> Result.success(result.data)
            is AppResult.Failure -> Result.failure(Exception("Tours error: ${result.error}"))
        }
    }

    override suspend fun getRecommendedHotels(): Result<List<Hotel>> {
        return when (val result = hotelRepo.getHotels()) {
            is AppResult.Success -> Result.success(result.data)
            is AppResult.Failure -> Result.failure(Exception("Hotels error: ${result.error}"))
        }
    }
}
