package com.softserveacademy.feature.profile.domain.usecases

import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.feature.profile.domain.repository.ProfileRepository

class GetProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<UserProfile> {
        return repository.getProfile()
    }
}
