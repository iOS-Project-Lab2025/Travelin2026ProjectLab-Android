package com.softserveacademy.core.error.util

import com.softserveacademy.core.error.mapper.ExceptionMapper
import com.softserveacademy.core.error.model.AppResult
import kotlinx.coroutines.CancellationException

suspend fun <T> safeCall(
    mapper: ExceptionMapper,
    block: suspend () -> T
): AppResult<T> = try {
    AppResult.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    AppResult.Failure(mapper.map(e))
}
