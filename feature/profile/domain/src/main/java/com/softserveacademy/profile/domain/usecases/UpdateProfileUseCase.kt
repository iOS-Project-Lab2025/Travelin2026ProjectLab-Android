package com.softserveacademy.profile.domain.usecases

import com.softserveacademy.profile.domain.model.UserProfile
import com.softserveacademy.profile.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Use case to update the user's profile information.
 */
class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    /**
     * Executes the profile update.
     * @param profile The updated profile data.
     * @param password The new password, if provided.
     * @return Result indicating success or failure.
     */
    suspend operator fun invoke(profile: UserProfile, password: String? = null): Result<Unit> {
        return repository.updateProfile(profile, password)
    }
}
