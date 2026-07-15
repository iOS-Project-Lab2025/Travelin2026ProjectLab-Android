package com.softserveacademy.home.domain.usecases

import com.softserveacademy.core.domain.model.UpcomingTrip
import com.softserveacademy.home.domain.repository.HomeRepository

class GetUpcomingTripUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<UpcomingTrip?> {
        return repository.getUpcomingTrip()
    }
}