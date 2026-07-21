package com.softserveacademy.home.data.repository

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.domain.model.Destination
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.HotelDetails
import com.softserveacademy.core.domain.model.IncludedItem
import com.softserveacademy.core.domain.model.Trip
import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.home.data.mockdata.HomeMockData
import com.softserveacademy.home.domain.repository.HomeRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

/**
 * Implementation of [HomeRepository] providing home screen data.
 * Currently uses mock data.
 */
class HomeRepositoryImpl @Inject constructor(
    private val hotelRepo: HotelRepo
) : HomeRepository {

    override suspend fun getUserProfile(): Result<UserProfile> {

        delay(500)

        return Result.success(
            HomeMockData.user
        )
    }

    override suspend fun getUpcomingTrip(): Result<Trip?> {

        delay(700)

        return Result.success(
            HomeMockData.trip
        )
    }

    override suspend fun getJourneyTogether(): Result<List<Destination>> {

        delay(700)

        return Result.success(
            HomeMockData.destinations
        )
    }

    override suspend fun getRecommendedHotels(): Result<List<Hotel>> {
        return Result.success(hotelRepo.getHotels())
    }
}