package com.softserveacademy.core.error.model

/** Domain-level model that classifies every application failure into a typed hierarchy. */
sealed interface AppError {

    sealed interface Network : AppError {
        data object NoConnection : Network
        data object Timeout : Network
        data class Server(val code: Int, val serverMessage: String? = null) : Network
    }

    sealed interface Auth : AppError {
        data object SessionExpired : Auth
        data object Unauthorized : Auth
    }

    sealed interface Data : AppError {
        data class NotFound(val resource: String) : Data
        data class Validation(val fields: Map<String, String>) : Data
    }

    data class Unknown(val throwable: Throwable) : AppError
}
