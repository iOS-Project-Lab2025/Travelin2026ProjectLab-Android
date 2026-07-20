package com.softserveacademy.home.data.repository

import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.home.domain.repository.ProfileRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

/**
 * Implementation of [ProfileRepository] providing profile data.
 * Currently uses mock data.
 */
class ProfileRepositoryImpl @Inject constructor() : ProfileRepository {

    private var cachedProfile = UserProfile(
        firstName = "John",
        lastName = "Doe",
        points = 100,
        avatarUrl = "https://example.com/avatar.jpg",
        phone = "+855 123 456 789",
        age = 30,
        location = "Mars, Solar System"
    )

    /**
     * Fetches mock profile data after a short delay.
     * @return A [Result] with the mock user profile.
     */
    override suspend fun getProfile(): Result<UserProfile> {
        delay(1000.milliseconds) // Simulate network delay
        return Result.success(cachedProfile)
    }

    /**
     * Updates mock profile data after a short delay.
     * @param profile The updated profile data.
     * @param password The new password, if provided.
     * @return A [Result] indicating success.
     */
    override suspend fun updateProfile(profile: UserProfile, password: String?): Result<Unit> {
        delay(1000.milliseconds) // Simulate network delay
        cachedProfile = profile
        return Result.success(Unit)
    }
}
