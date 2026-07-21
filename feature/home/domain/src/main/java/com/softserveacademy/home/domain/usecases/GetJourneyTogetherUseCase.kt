package com.softserveacademy.home.domain.usecases

import com.softserveacademy.core.domain.model.Destination
import com.softserveacademy.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetJourneyTogetherUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<Destination>> {
        return repository.getJourneyTogether()
    }
}