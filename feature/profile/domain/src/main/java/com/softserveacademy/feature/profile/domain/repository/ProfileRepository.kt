package com.softserveacademy.feature.profile.domain.repository

import com.softserveacademy.core.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getProfile(): Result<UserProfile>
}
