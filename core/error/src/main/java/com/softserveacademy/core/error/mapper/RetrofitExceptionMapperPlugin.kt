package com.softserveacademy.core.error.mapper

import com.softserveacademy.core.error.model.AppError
import javax.inject.Inject
import javax.inject.Singleton

/** [ExceptionMapperPlugin] that maps [retrofit2.HttpException] to [AppError] using reflection. */
@Singleton
class RetrofitExceptionMapperPlugin @Inject constructor() : ExceptionMapperPlugin {

    override fun map(throwable: Throwable): AppError? {
        val httpExceptionClass = try {
            Class.forName("retrofit2.HttpException")
        } catch (_: ClassNotFoundException) {
            return null
        }

        if (!httpExceptionClass.isInstance(throwable)) return null

        val code = httpExceptionClass.getMethod("code").invoke(throwable) as Int
        val message = httpExceptionClass.getMethod("message").invoke(throwable) as? String

        return mapHttpCode(code, message)
    }

    private fun mapHttpCode(code: Int, message: String?): AppError = when (code) {
        401 -> AppError.Auth.SessionExpired
        403 -> AppError.Auth.Unauthorized
        404 -> AppError.Data.NotFound("resource")
        in 500..599 -> AppError.Network.Server(code, message)
        else -> AppError.Network.Server(code, message)
    }
}
