package com.softserveacademy.core.error.handler

import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.ErrorAction

interface ErrorHandler {
    fun handle(error: AppError): ErrorAction
}
