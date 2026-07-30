package com.softserveacademy.core.error.extension

import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.AppResult

inline fun <T> AppResult<T>.onSuccess(action: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) action(data)
    return this
}

inline fun <T> AppResult<T>.onFailure(action: (AppError) -> Unit): AppResult<T> {
    if (this is AppResult.Failure) action(error)
    return this
}

inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> = when (this) {
    is AppResult.Success -> AppResult.Success(transform(data))
    is AppResult.Failure -> this
}

inline fun <T, R> AppResult<T>.flatMap(transform: (T) -> AppResult<R>): AppResult<R> = when (this) {
    is AppResult.Success -> transform(data)
    is AppResult.Failure -> this
}

inline fun <T> AppResult<T>.recover(transform: (AppError) -> AppResult<T>): AppResult<T> = when (this) {
    is AppResult.Success -> this
    is AppResult.Failure -> transform(error)
}

fun <T> AppResult<T>.getOrNull(): T? = when (this) {
    is AppResult.Success -> data
    is AppResult.Failure -> null
}

fun <T> AppResult<T>.getOrDefault(default: T): T = when (this) {
    is AppResult.Success -> data
    is AppResult.Failure -> default
}
