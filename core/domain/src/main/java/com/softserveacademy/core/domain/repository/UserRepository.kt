package com.softserveacademy.core.domain.repository

import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.core.error.model.AppResult

interface UserRepository {

    suspend fun getUserProfile(): AppResult<UserProfile>
}