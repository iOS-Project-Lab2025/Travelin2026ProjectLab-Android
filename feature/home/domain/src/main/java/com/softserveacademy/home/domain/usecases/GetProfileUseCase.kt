package com.softserveacademy.home.domain.usecases

import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.home.domain.repository.ProfileRepository

/**
 * Use case to retrieve the user's profile information.
 */
class GetProfileUseCase(
    private val repository: ProfileRepository
) {
    /**
     * Executes the profile retrieval.
     * @return Result containing [UserProfile] on success.
     */
    suspend operator fun invoke(): Result<UserProfile> {
        return repository.getProfile()
    }
}
