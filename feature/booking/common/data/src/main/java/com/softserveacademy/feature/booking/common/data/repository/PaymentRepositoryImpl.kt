package com.softserveacademy.feature.booking.common.data.repository

import com.softserveacademy.core.error.mapper.ExceptionMapper
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.core.error.util.safeCall
import com.softserveacademy.feature.booking.common.data.BuildConfig
import com.softserveacademy.feature.booking.common.data.remote.StripeApi
import com.softserveacademy.feature.booking.common.domain.repository.PaymentRepository
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val stripeApi: StripeApi,
    private val mapper: ExceptionMapper
) : PaymentRepository {
    override suspend fun getClientSecret(amount: Long, currency: String): AppResult<String> = safeCall(mapper) {
        val response = stripeApi.createPaymentIntent(
            apiKey = "Bearer ${BuildConfig.STRIPE_SECRET_KEY}",
            amount = amount,
            currency = currency
        )
        response["client_secret"] as String
    }
}
