package com.softserveacademy.profile.domain.usecases

import com.softserveacademy.core.domain.util.Validator
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
        val birthDate = profile.birthDate ?: return Result.failure(IllegalArgumentException("Birth date is required"))
        
        if (!Validator.isAtLeast18(birthDate)) {
            return Result.failure(IllegalArgumentException("You must be at least 18 years old"))
        }

        if (password != null && password.length < 6) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters long"))
        }

        return repository.updateProfile(profile, password)
    }
}
