package com.softserveacademy.feature.booking.common.domain.repository

import com.softserveacademy.core.error.model.AppResult

interface PaymentRepository {
    suspend fun getClientSecret(amount: Long, currency: String): AppResult<String>
}
