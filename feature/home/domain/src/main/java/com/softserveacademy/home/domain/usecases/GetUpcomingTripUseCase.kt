package com.softserveacademy.home.domain.usecases

import com.softserveacademy.core.domain.model.Trip
import com.softserveacademy.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetUpcomingTripUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<Trip?> {
        return repository.getUpcomingTrip()
    }
}