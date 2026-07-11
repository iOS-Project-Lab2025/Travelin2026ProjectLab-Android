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
}
