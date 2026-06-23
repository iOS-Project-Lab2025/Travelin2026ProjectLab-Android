package com.softserveacademy.core.domain.repository

import com.softserveacademy.core.domain.model.User

interface AuthRepository {

    suspend fun register(user: User, password: String): Result<Unit>

    suspend fun login(email: String, password: String): Result<Unit>

    suspend fun recoverPassword(email: String): Result<Unit>
}