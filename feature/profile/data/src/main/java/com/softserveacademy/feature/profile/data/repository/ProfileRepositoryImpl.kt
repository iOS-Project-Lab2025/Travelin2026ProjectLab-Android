package com.softserveacademy.feature.profile.data.repository

import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class ProfileRepositoryImpl @Inject constructor() : ProfileRepository {

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
