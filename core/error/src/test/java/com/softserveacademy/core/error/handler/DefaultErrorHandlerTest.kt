package com.softserveacademy.core.error.handler

import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.ErrorAction
import com.softserveacademy.core.error.model.UiText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultErrorHandlerTest {

    private val handler = DefaultErrorHandler(GlobalErrorHandler())

    private fun rawMessage(action: ErrorAction): UiText.Raw {
        assertTrue(action is ErrorAction.ShowMessage)
        return (action as ErrorAction.ShowMessage).message as UiText.Raw
    }

    @Test
    fun `handle should show no connection message for network no connection`() {
        // When
        val action = handler.handle(AppError.Network.NoConnection)

        // Then
        assertEquals(UiText.Raw("No internet connection. Please check your network."), rawMessage(action))
    }

    @Test
    fun `handle should show timeout message for network timeout`() {
        // When
        val action = handler.handle(AppError.Network.Timeout)

        // Then
        assertEquals(UiText.Raw("Request timed out. Please try again."), rawMessage(action))
    }

    @Test
    fun `handle should show server error message with code for network server`() {
        // When
        val action = handler.handle(AppError.Network.Server(code = 500, serverMessage = "Internal"))

        // Then
        assertEquals(UiText.Raw("Server error (500). Please try again later."), rawMessage(action))
    }

    @Test
    fun `handle should navigate to login for session expired`() {
        // When
        val action = handler.handle(AppError.Auth.SessionExpired)

        // Then
        assertEquals(ErrorAction.Navigate("login"), action)
    }

    @Test
    fun `handle should show permission message for unauthorized`() {
        // When
        val action = handler.handle(AppError.Auth.Unauthorized)

        // Then
        assertEquals(UiText.Raw("You don't have permission to perform this action."), rawMessage(action))
    }

    @Test
    fun `handle should show not found message with resource for not found`() {
        // When
        val action = handler.handle(AppError.Data.NotFound("hotel"))

        // Then
        assertEquals(UiText.Raw("Requested hotel was not found."), rawMessage(action))
    }

    @Test
    fun `handle should show formatted validation messages for validation`() {
        // When
        val action = handler.handle(
            AppError.Data.Validation(mapOf("email" to "invalid", "name" to "required"))
        )

        // Then
        assertEquals(UiText.Raw("email: invalid\nname: required"), rawMessage(action))
    }

    @Test
    fun `handle should show unexpected error message for unknown`() {
        // When
        val action = handler.handle(AppError.Unknown(RuntimeException("boom")))

        // Then
        assertEquals(UiText.Raw("An unexpected error occurred."), rawMessage(action))
    }
}