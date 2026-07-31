package com.softserveacademy.core.error.model

/** Wrapper that models the outcome of an operation as either success or failure. */
sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    data class Failure(val error: AppError) : AppResult<Nothing>
}
