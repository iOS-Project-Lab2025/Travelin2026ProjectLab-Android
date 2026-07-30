package com.softserveacademy.core.error.mapper

import com.softserveacademy.core.error.model.AppError
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExceptionMapper @Inject constructor(
    private val customMappers: Set<@JvmSuppressWildcards ExceptionMapperPlugin>
) {

    fun map(throwable: Throwable): AppError {
        for (plugin in customMappers) {
            plugin.map(throwable)?.let { return it }
        }
        return mapDefault(throwable)
    }

    private fun mapDefault(throwable: Throwable): AppError = when (throwable) {
        is SocketTimeoutException -> AppError.Network.Timeout
        is IOException -> AppError.Network.NoConnection
        else -> AppError.Unknown(throwable)
    }
}
