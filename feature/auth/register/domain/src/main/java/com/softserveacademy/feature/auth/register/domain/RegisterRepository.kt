package com.softserveacademy.feature.auth.register.domain

import com.softserveacademy.core.domain.model.User

interface RegisterRepository {
    suspend fun register(user: User, password: String): Result<Unit>
}
