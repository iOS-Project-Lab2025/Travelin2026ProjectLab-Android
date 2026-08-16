package com.softserveacademy.core.error.util

import com.softserveacademy.core.error.mapper.ExceptionMapper
import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.AppResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class SafeFlowTest {

    private val mapper = ExceptionMapper(emptySet())

    @Test
    fun `safeFlow should wrap each emission in success`() = runTest {
        // When
        val result = flowOf(1, 2, 3).safeFlow(mapper).toList()

        // Then
        assertEquals(
            listOf(
                AppResult.Success(1),
                AppResult.Success(2),
                AppResult.Success(3)
            ),
            result
        )
    }

    @Test
    fun `safeFlow should emit failure when upstream throws an exception`() = runTest {
        // When
        val result = flow {
            emit(1)
            throw IOException("boom")
        }.safeFlow(mapper).toList()

        // Then
        assertEquals(
            listOf(
                AppResult.Success(1),
                AppResult.Failure(AppError.Network.NoConnection)
            ),
            result
        )
    }

    @Test
    fun `safeFlow should rethrow non exception throwable`() = runTest {
        // Given
        val expected = AssertionError("boom")
        var thrown: Throwable? = null

        // When
        try {
            flow<Int> { throw expected }.safeFlow(mapper).toList()
        } catch (e: AssertionError) {
            thrown = e
        }

        // Then
        assertEquals(expected, thrown)
    }
}