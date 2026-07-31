package com.softserveacademy.feature.booking.common.domain.repository

interface PaymentRepository {
    suspend fun getClientSecret(amount: Long, currency: String): Result<String>
}
