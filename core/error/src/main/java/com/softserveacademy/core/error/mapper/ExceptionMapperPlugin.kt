package com.softserveacademy.core.error.mapper

import com.softserveacademy.core.error.model.AppError

/**
 * Plugin interface for the chain-of-responsibility pattern used by [ExceptionMapper].
 */
interface ExceptionMapperPlugin {
    fun map(throwable: Throwable): AppError?
}
