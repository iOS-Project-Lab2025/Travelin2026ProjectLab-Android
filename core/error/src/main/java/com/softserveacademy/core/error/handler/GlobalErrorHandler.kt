package com.softserveacademy.core.error.handler

import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.ErrorAction
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

/** Singleton that collects global-level errors (e.g. auth/session) and exposes them as a [Flow]. */
@Singleton
class GlobalErrorHandler @Inject constructor() {

    private val _errors = Channel<ErrorAction>(Channel.BUFFERED)
    val errors: Flow<ErrorAction> = _errors.receiveAsFlow()

    suspend fun dispatch(action: ErrorAction) {
        _errors.send(action)
    }

    suspend fun dispatchAuth(error: AppError.Auth) {
        val action = when (error) {
            is AppError.Auth.SessionExpired -> ErrorAction.Navigate("login")
            is AppError.Auth.Unauthorized -> ErrorAction.Navigate("unauthorized")
        }
        dispatch(action)
    }
}
