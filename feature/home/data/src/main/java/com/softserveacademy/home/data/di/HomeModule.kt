package com.softserveacademy.home.data.di

import com.softserveacademy.home.data.repository.HomeRepositoryImpl
import com.softserveacademy.home.domain.HomeRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    abstract fun bindHomeRepository(
        impl: HomeRepositoryImpl
    ): HomeRepository
}