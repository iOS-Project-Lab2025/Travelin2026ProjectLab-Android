package com.softserveacademy.feature.booking.common.data.repository

import com.softserveacademy.feature.booking.common.data.BuildConfig
import com.softserveacademy.feature.booking.common.data.remote.StripeApi
import com.softserveacademy.feature.booking.common.domain.repository.PaymentRepository
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val stripeApi: StripeApi
) : PaymentRepository {
    override suspend fun getClientSecret(amount: Long, currency: String): Result<String> {
        return runCatching {
            val response = stripeApi.createPaymentIntent(
                apiKey = "Bearer ${BuildConfig.STRIPE_SECRET_KEY}",
                amount = amount,
                currency = currency
            )
            response["client_secret"] as String
        }
    }
}
