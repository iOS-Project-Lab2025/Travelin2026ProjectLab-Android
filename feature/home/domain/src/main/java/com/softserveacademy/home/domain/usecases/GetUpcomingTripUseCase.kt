package com.softserveacademy.home.domain.usecases

import com.softserveacademy.core.domain.model.Trip
import com.softserveacademy.home.domain.repository.HomeRepository

class GetUpcomingTripUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<Trip?> {
        return repository.getUpcomingTrip()
    }
}