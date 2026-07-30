package com.softserveacademy.core.error.handler

import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.ErrorAction
import com.softserveacademy.core.error.model.UiText
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultErrorHandler @Inject constructor(
    private val globalErrorHandler: GlobalErrorHandler
) : ErrorHandler {

    override fun handle(error: AppError): ErrorAction = when (error) {
        is AppError.Network.NoConnection -> ErrorAction.ShowMessage(
            UiText.Raw("No internet connection. Please check your network.")
        )
        is AppError.Network.Timeout -> ErrorAction.ShowMessage(
            UiText.Raw("Request timed out. Please try again.")
        )
        is AppError.Network.Server -> ErrorAction.ShowMessage(
            UiText.Raw("Server error (${error.code}). Please try again later.")
        )
        is AppError.Auth.SessionExpired -> ErrorAction.Navigate("login")
        is AppError.Auth.Unauthorized -> ErrorAction.ShowMessage(
            UiText.Raw("You don't have permission to perform this action.")
        )
        is AppError.Data.NotFound -> ErrorAction.ShowMessage(
            UiText.Raw("Requested ${error.resource} was not found.")
        )
        is AppError.Data.Validation -> ErrorAction.ShowMessage(
            UiText.Raw(formatValidationErrors(error.fields))
        )
        is AppError.Unknown -> ErrorAction.ShowMessage(
            UiText.Raw("An unexpected error occurred.")
        )
    }

    private fun formatValidationErrors(fields: Map<String, String>): String =
        fields.entries.joinToString("\n") { (field, message) -> "$field: $message" }
}
