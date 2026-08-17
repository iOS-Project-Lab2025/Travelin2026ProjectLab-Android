package com.softserveacademy.core.error.handler

import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.ErrorAction

/**
 * Screen-level contract for translating an [AppError] into an [ErrorAction] the UI can perform.
 */
interface ErrorHandler {
    fun handle(error: AppError): ErrorAction
}
