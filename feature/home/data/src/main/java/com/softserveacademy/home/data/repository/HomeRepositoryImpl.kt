package com.softserveacademy.home.data.repository

import com.softserveacademy.core.domain.model.Destination
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.Trip
import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val hotelRepo: HotelRepo
) : HomeRepository {

    override suspend fun getUserProfile(): Result<UserProfile> {
        TODO("Not yet implemented")
    }

    override suspend fun getUpcomingTrip(): Result<Trip?> {
        TODO("Not yet implemented")
    }

    override suspend fun getJourneyTogether(): Result<List<Destination>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecommendedHotels(): Result<List<Hotel>> {
        return Result.success(hotelRepo.getHotels())
    }
}
