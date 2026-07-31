package com.softserveacademy.core.data.di

import com.softserveacademy.core.error.handler.DefaultErrorHandler
import com.softserveacademy.core.error.handler.ErrorHandler
import com.softserveacademy.core.error.mapper.ExceptionMapperPlugin
import com.softserveacademy.core.error.mapper.RetrofitExceptionMapperPlugin
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dagger.multibindings.Multibinds
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorDataModule {

    @Multibinds
    abstract fun bindExceptionMapperPlugins(): Set<ExceptionMapperPlugin>

    @Binds
    @Singleton
    abstract fun bindErrorHandler(impl: DefaultErrorHandler): ErrorHandler

    @Binds
    @IntoSet
    abstract fun bindRetrofitMapper(impl: RetrofitExceptionMapperPlugin): ExceptionMapperPlugin
}
