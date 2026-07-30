package com.softserveacademy.core.error.extension

import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.AppResult

/** Invokes [action] with the value if this is a success, then returns this result unchanged. */
inline fun <T> AppResult<T>.onSuccess(action: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) action(data)
    return this
}

/** Invokes [action] with the error if this is a failure, then returns this result unchanged. */
inline fun <T> AppResult<T>.onFailure(action: (AppError) -> Unit): AppResult<T> {
    if (this is AppResult.Failure) action(error)
    return this
}

/** Transforms the success value using [transform] or passes through the failure. */
inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> = when (this) {
    is AppResult.Success -> AppResult.Success(transform(data))
    is AppResult.Failure -> this
}

/** Transforms the success value using [transform] which returns another [AppResult], or passes through the failure. */
inline fun <T, R> AppResult<T>.flatMap(transform: (T) -> AppResult<R>): AppResult<R> = when (this) {
    is AppResult.Success -> transform(data)
    is AppResult.Failure -> this
}

/** Recovers from a failure by applying [transform] to the error, or passes through the success. */
inline fun <T> AppResult<T>.recover(transform: (AppError) -> AppResult<T>): AppResult<T> = when (this) {
    is AppResult.Success -> this
    is AppResult.Failure -> transform(error)
}

/** Returns the success value or `null` if this is a failure. */
fun <T> AppResult<T>.getOrNull(): T? = when (this) {
    is AppResult.Success -> data
    is AppResult.Failure -> null
}

/** Returns the success value or [default] if this is a failure. */
fun <T> AppResult<T>.getOrDefault(default: T): T = when (this) {
    is AppResult.Success -> data
    is AppResult.Failure -> default
}
