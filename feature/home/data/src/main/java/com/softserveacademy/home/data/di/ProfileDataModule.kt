package com.softserveacademy.home.data.di

import com.softserveacademy.home.data.repository.ProfileRepositoryImpl
import com.softserveacademy.home.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing profile data dependencies within the home module.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileDataModule {

    /**
     * Binds the [ProfileRepositoryImpl] to the [ProfileRepository] interface.
     */
    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository
}
