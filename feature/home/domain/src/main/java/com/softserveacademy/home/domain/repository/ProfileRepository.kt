package com.softserveacademy.home.domain.repository

import com.softserveacademy.core.domain.model.UserProfile

/**
 * Repository interface for profile-related data operations.
 */
interface ProfileRepository {
    /**
     * Fetches the current user's profile information.
     * @return Result containing [UserProfile] on success.
     */
    suspend fun getProfile(): Result<UserProfile>

    /**
     * Updates the user's profile information.
     * @param profile The updated profile data.
     * @param password The new password, if provided.
     * @return Result indicating success or failure.
     */
    suspend fun updateProfile(profile: UserProfile, password: String?): Result<Unit>
}
