package com.softserveacademy.home.domain.usecases

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetRecommendedHotelsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<Hotel>> {
        return repository.getRecommendedHotels()
    }
}