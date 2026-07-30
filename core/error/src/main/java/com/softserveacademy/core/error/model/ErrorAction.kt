package com.softserveacademy.core.error.model

sealed interface ErrorAction {
    data class ShowMessage(val message: UiText) : ErrorAction
    data class Navigate(val route: String) : ErrorAction
    data class Retry(val message: UiText, val retryAction: suspend () -> Unit) : ErrorAction
    data object Silent : ErrorAction
}
