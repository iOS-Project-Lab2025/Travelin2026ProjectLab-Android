package com.softserveacademy.core.domain.repository

import com.softserveacademy.core.domain.model.Trip

interface TripRepository {

    suspend fun getUpcomingTrip(): Result<Trip?>

    suspend fun getPastTrips(): Result<List<Trip>>

    suspend fun getTrip(id: String): Result<Trip>

    suspend fun cancelTrip(id: String): Result<Unit>

}