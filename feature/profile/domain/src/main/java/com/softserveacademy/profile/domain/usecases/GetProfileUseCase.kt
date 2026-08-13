package com.softserveacademy.profile.domain.usecases

import com.softserveacademy.profile.domain.model.UserProfile
import com.softserveacademy.profile.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Use case to retrieve the user's profile information.
 */
class GetProfileUseCase @Inject constructor(
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
