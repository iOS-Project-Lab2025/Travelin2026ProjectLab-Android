package com.softserveacademy.home.domain

import com.softserveacademy.core.domain.model.Destination
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.UpcomingTrip
import com.softserveacademy.core.domain.model.UserProfile

interface HomeRepository {
    suspend fun getUserProfile(): Result<UserProfile>
    suspend fun getUpcomingTrip(): Result<UpcomingTrip?>
    suspend fun getJourneyTogether(): Result<List<Destination>>
    suspend fun getRecommendedHotels(): Result<List<Hotel>>
}