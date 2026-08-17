package com.softserveacademy.core.error.extension

import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.AppResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class ResultExtensionsTest {

    @Test
    fun `onSuccess should invoke action with data when result is success`() {
        // Given
        var received: Int? = null

        // When
        AppResult.Success(42).onSuccess { received = it }

        // Then
        assertEquals(42, received)
    }

    @Test
    fun `onSuccess should not invoke action when result is failure`() {
        // Given
        var invoked = false
        val failure: AppResult<Int> = AppResult.Failure(AppError.Network.NoConnection)

        // When
        failure.onSuccess { invoked = true }

        // Then
        assertFalse(invoked)
    }

    @Test
    fun `onSuccess should return same result unchanged`() {
        // Given
        val success: AppResult<Int> = AppResult.Success(7)

        // When
        val result = success.onSuccess {}

        // Then
        assertEquals(success, result)
    }

    @Test
    fun `onFailure should invoke action with error when result is failure`() {
        // Given
        val error = AppError.Network.Timeout
        val failure: AppResult<Int> = AppResult.Failure(error)
        var received: AppError? = null

        // When
        failure.onFailure { received = it }

        // Then
        assertEquals(error, received)
    }

    @Test
    fun `onFailure should not invoke action when result is success`() {
        // Given
        var invoked = false

        // When
        AppResult.Success(1).onFailure { invoked = true }

        // Then
        assertFalse(invoked)
    }

    @Test
    fun `onFailure should return same result unchanged`() {
        // Given
        val failure: AppResult<Int> = AppResult.Failure(AppError.Network.NoConnection)

        // When
        val result = failure.onFailure {}

        // Then
        assertEquals(failure, result)
    }

    @Test
    fun `map should transform success value when result is success`() {
        // When
        val result = AppResult.Success(2).map { it * 10 }

        // Then
        assertEquals(AppResult.Success(20), result)
    }

    @Test
    fun `map should pass failure through unchanged`() {
        // Given
        val failure: AppResult<Int> = AppResult.Failure(AppError.Network.NoConnection)

        // When
        val result = failure.map { it * 10 }

        // Then
        assertEquals(failure, result)
    }

    @Test
    fun `flatMap should apply transform returning another result when result is success`() {
        // When
        val result = AppResult.Success(3).flatMap { AppResult.Success(it + 1) }

        // Then
        assertEquals(AppResult.Success(4), result)
    }

    @Test
    fun `flatMap should pass failure through unchanged`() {
        // Given
        val failure: AppResult<Int> = AppResult.Failure(AppError.Network.Timeout)

        // When
        val result = failure.flatMap { AppResult.Success(it + 1) }

        // Then
        assertEquals(failure, result)
    }

    @Test
    fun `recover should apply transform to error when result is failure`() {
        // When
        val result: AppResult<Int> = AppResult.Failure(AppError.Network.NoConnection)
            .recover { AppResult.Success(99) }

        // Then
        assertEquals(AppResult.Success(99), result)
    }

    @Test
    fun `recover should pass success through unchanged`() {
        // Given
        val success: AppResult<Int> = AppResult.Success(5)

        // When
        val result = success.recover { AppResult.Success(99) }

        // Then
        assertEquals(success, result)
    }

    @Test
    fun `getOrNull should return data when result is success`() {
        // When
        val value = AppResult.Success(42).getOrNull()

        // Then
        assertEquals(42, value)
    }

    @Test
    fun `getOrNull should return null when result is failure`() {
        // Given
        val failure: AppResult<Int> = AppResult.Failure(AppError.Network.NoConnection)

        // When
        val value = failure.getOrNull()

        // Then
        assertNull(value)
    }

    @Test
    fun `getOrDefault should return data when result is success`() {
        // When
        val value = AppResult.Success(1).getOrDefault(10)

        // Then
        assertEquals(1, value)
    }

    @Test
    fun `getOrDefault should return default when result is failure`() {
        // Given
        val failure: AppResult<Int> = AppResult.Failure(AppError.Network.NoConnection)

        // When
        val value = failure.getOrDefault(10)

        // Then
        assertEquals(10, value)
    }
}