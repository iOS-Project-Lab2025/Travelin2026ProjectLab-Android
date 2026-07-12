package com.softserveacademy.home.domain.usecases

import com.softserveacademy.core.domain.model.HomeHotel
import com.softserveacademy.home.domain.HomeRepository

class GetRecommendedHotelsUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<HomeHotel>> {
        return repository.getRecommendedHotels()
    }
}