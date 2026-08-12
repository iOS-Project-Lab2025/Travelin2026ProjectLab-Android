package com.softserveacademy.core.data.di

import android.content.Context
import android.content.pm.PackageManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
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

    @Provides
    @Singleton
    fun providePlacesClient(@ApplicationContext context: Context): PlacesClient {
        if (!Places.isInitialized()) {
            val ai = try {
                context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            } catch (e: Exception) {
                null
            }
            val apiKey = ai?.metaData?.getString("com.google.android.geo.API_KEY")

            val keyToUse = if (!apiKey.isNullOrBlank() && apiKey != "YOUR_API_KEY") {
                apiKey
            } else {
                "MISSING_API_KEY"
            }
            Places.initialize(context, keyToUse)
        }
        return Places.createClient(context)
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
