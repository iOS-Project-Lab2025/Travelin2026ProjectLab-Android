package com.softserveacademy.home.domain.repository

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.domain.model.Trip
import com.softserveacademy.core.domain.model.UserProfile

interface HomeRepository {
    suspend fun getUserProfile(): Result<UserProfile>
    suspend fun getUpcomingTrip(): Result<Trip?>
    suspend fun getJourneyTogether(): Result<List<Tour>>
    suspend fun getRecommendedHotels(): Result<List<Hotel>>
}