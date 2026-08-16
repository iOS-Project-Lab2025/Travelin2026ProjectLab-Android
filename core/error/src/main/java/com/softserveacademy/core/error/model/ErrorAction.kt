package com.softserveacademy.core.error.model

/**
 * Describes what the UI layer should do in response to an error.
 */
sealed interface ErrorAction {
    data class ShowMessage(val message: UiText) : ErrorAction
    data class Navigate(val route: String) : ErrorAction
    data class Retry(val message: UiText, val retryAction: suspend () -> Unit) : ErrorAction
    data object Silent : ErrorAction
}
