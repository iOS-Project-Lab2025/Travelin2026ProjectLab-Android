package com.softserveacademy.core.domain.repository

import com.softserveacademy.core.domain.model.UserProfile

interface UserRepository {

    suspend fun getUserProfile(): Result<UserProfile>
}