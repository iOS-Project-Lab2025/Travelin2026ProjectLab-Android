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

    /**
     * Fetches mock profile data after a short delay.
     * @return A [Result] with the mock user profile.
     */
    override suspend fun getProfile(): Result<UserProfile> {
        delay(1000.milliseconds) // Simulate network delay
        return Result.success(
            UserProfile(
                name = "John Doe",
                points = 100,
                avatarUrl = "https://example.com/avatar.jpg", // Placeholder
                location = "Mars, Solar System"
            )
        )
    }
}
