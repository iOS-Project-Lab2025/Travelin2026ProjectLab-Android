package com.softserveacademy.core.data.di

import com.softserveacademy.core.data.repository.AiAssistantRepositoryImpl
import com.softserveacademy.core.domain.repository.AiAssistantRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing AI Assistant related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AiDataModule {
    
    @Binds
    @Singleton
    abstract fun bindAiAssistantRepository(
        impl: AiAssistantRepositoryImpl
    ): AiAssistantRepository
}
