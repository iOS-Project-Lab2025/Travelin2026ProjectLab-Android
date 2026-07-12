package com.softserveacademy.home.data.di

import com.softserveacademy.home.data.repository.HomeRepositoryImpl
import com.softserveacademy.home.domain.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    abstract fun bindHomeRepository(
        impl: HomeRepositoryImpl
    ): HomeRepository
}