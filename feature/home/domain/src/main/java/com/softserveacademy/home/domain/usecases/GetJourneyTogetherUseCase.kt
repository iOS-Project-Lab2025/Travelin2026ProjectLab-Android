package com.softserveacademy.home.domain.usecases

import com.softserveacademy.core.domain.model.Destination
import com.softserveacademy.home.domain.repository.HomeRepository

class GetJourneyTogetherUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<Destination>> {
        return repository.getJourneyTogether()
    }
}