package com.softserveacademy.core.error.util

import com.softserveacademy.core.error.mapper.ExceptionMapper
import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.AppResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class SafeCallTest {

    private val mapper = ExceptionMapper(emptySet())

    @Test
    fun `safeCall should return success with block result when no exception is thrown`() = runTest {
        // When
        val result = safeCall(mapper) { 42 }

        // Then
        assertEquals(AppResult.Success(42), result)
    }

    @Test
    fun `safeCall should return failure with mapped error when block throws`() = runTest {
        // When
        val result = safeCall(mapper) { throw IOException("boom") }

        // Then
        assertEquals(AppResult.Failure(AppError.Network.NoConnection), result)
    }

    @Test
    fun `safeCall should rethrow cancellation exception`() = runTest {
        // Given
        val expected = CancellationException("cancelled")
        var thrown: Throwable? = null

        // When
        try {
            safeCall(mapper) { throw expected }
        } catch (e: CancellationException) {
            thrown = e
        }

        // Then
        assertEquals(expected, thrown)
    }
}