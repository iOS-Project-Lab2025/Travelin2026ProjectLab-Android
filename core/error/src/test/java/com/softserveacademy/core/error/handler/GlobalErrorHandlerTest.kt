package com.softserveacademy.core.error.handler

import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.ErrorAction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GlobalErrorHandlerTest {

    @Test
    fun `dispatch should expose action through errors flow`() = runTest {
        // Given
        val handler = GlobalErrorHandler()
        val action = ErrorAction.Silent

        // When
        handler.dispatch(action)

        // Then
        assertEquals(action, handler.errors.first())
    }

    @Test
    fun `dispatchAuth should navigate to login when session expired`() = runTest {
        // Given
        val handler = GlobalErrorHandler()

        // When
        handler.dispatchAuth(AppError.Auth.SessionExpired)

        // Then
        assertEquals(ErrorAction.Navigate("login"), handler.errors.first())
    }

    @Test
    fun `dispatchAuth should navigate to unauthorized when unauthorized`() = runTest {
        // Given
        val handler = GlobalErrorHandler()

        // When
        handler.dispatchAuth(AppError.Auth.Unauthorized)

        // Then
        assertEquals(ErrorAction.Navigate("unauthorized"), handler.errors.first())
    }
}