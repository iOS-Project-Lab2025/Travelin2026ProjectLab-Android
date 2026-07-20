package com.softserveacademy.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.softserveacademy.core.data.repository.CorePreferencesRepositoryImpl
import com.softserveacademy.core.domain.repository.CorePreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}

/**
 * Module for providing dependencies related to CorePreferencesRepository.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class CoreDataModule {

    @Binds
    @Singleton
    abstract fun bindCorePreferencesRepository(
        impl: CorePreferencesRepositoryImpl
    ): CorePreferencesRepository
}
