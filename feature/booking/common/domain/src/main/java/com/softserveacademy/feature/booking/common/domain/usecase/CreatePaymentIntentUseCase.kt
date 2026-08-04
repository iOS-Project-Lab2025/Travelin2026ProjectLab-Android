package com.softserveacademy.feature.booking.common.domain.usecase

import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.feature.booking.common.domain.repository.PaymentRepository
import javax.inject.Inject

class CreatePaymentIntentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(amount: Long, currency: String): AppResult<String> {
        return paymentRepository.getClientSecret(amount, currency)
    }
}
