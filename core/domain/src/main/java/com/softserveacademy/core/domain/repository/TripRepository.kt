package com.softserveacademy.core.domain.repository

import com.softserveacademy.core.domain.model.Trip
import com.softserveacademy.core.error.model.AppResult

interface TripRepository {

    suspend fun getUpcomingTrip(): AppResult<Trip?>

    suspend fun getPastTrips(): AppResult<List<Trip>>

    suspend fun getTrip(id: String): AppResult<Trip>

    suspend fun cancelTrip(id: String): AppResult<Unit>

}