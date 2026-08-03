package com.softserveacademy.core.error.util

import com.softserveacademy.core.error.mapper.ExceptionMapper
import com.softserveacademy.core.error.model.AppResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Wraps a [Flow] and maps its emissions to [AppResult].
 * Catches exceptions and maps them to [AppError] via [mapper].
 */
fun <T> Flow<T>.safeFlow(mapper: ExceptionMapper): Flow<AppResult<T>> = this
    .map { AppResult.Success(it) as AppResult<T> }
    .catch { e ->
        if (e is Exception) {
            emit(AppResult.Failure(mapper.map(e)))
        } else throw e
    }
