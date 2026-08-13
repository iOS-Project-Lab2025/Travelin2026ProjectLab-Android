package com.softserveacademy.core.data.repository

import com.softserveacademy.core.domain.repository.PoiRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PoiDataModule {

    @Binds
    @Singleton
    abstract fun bindPoiRepo(
        poiRepoImpl: PoiRepoImpl
    ): PoiRepo
}
