package com.softserveacademy.feature.auth.common.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.softserveacademy.feature.auth.common.data.SessionRepositoryImpl
import com.softserveacademy.feature.auth.common.domain.CheckSessionUseCase
import com.softserveacademy.feature.auth.common.domain.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthDataModule {

    @Provides
    @Singleton
    fun provideSessionRepository(dataStore: DataStore<Preferences>): SessionRepository {
        return SessionRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideCheckSessionUseCase(repository: SessionRepository): CheckSessionUseCase {
        return CheckSessionUseCase(repository)
    }
}
