package com.softserveacademy.home.domain.usecases

import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.home.domain.repository.HomeRepository

class GetUserProfileUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<UserProfile> {
        return repository.getUserProfile()
    }
}