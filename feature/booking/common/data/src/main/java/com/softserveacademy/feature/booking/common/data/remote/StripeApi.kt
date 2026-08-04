package com.softserveacademy.feature.booking.common.data.remote

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface StripeApi {
    @FormUrlEncoded
    @POST("v1/payment_intents")
    suspend fun createPaymentIntent(
        @Header("Authorization") apiKey: String,
        @Field("amount") amount: Long,
        @Field("currency") currency: String
    ): Map<String, Any>
}
