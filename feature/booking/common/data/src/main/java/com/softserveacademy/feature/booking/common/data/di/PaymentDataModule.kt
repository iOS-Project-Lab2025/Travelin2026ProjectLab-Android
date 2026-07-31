package com.softserveacademy.feature.booking.common.data.di

import com.softserveacademy.feature.booking.common.data.remote.StripeApi
import com.softserveacademy.feature.booking.common.data.repository.PaymentRepositoryImpl
import com.softserveacademy.feature.booking.common.domain.repository.PaymentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PaymentDataModule {

    @Provides
    @Singleton
    fun provideStripeApi(): StripeApi {
        return Retrofit.Builder()
            .baseUrl("https://api.stripe.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StripeApi::class.java)
    }

    @Provides
    @Singleton
    fun providePaymentRepository(stripeApi: StripeApi): PaymentRepository {
        return PaymentRepositoryImpl(stripeApi)
    }
}
