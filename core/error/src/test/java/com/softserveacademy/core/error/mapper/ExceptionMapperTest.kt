package com.softserveacademy.core.error.mapper

import com.softserveacademy.core.error.model.AppError
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException

class ExceptionMapperTest {

    private val emptyMapper = ExceptionMapper(emptySet())

    @Test
    fun `map should return network timeout for socket timeout exception`() {
        // When
        val result = emptyMapper.map(SocketTimeoutException("timeout"))

        // Then
        assertEquals(AppError.Network.Timeout, result)
    }

    @Test
    fun `map should return no connection for io exception`() {
        // When
        val result = emptyMapper.map(IOException("no network"))

        // Then
        assertEquals(AppError.Network.NoConnection, result)
    }

    @Test
    fun `map should return unknown for generic throwable`() {
        // Given
        val throwable = IllegalArgumentException("boom")

        // When
        val result = emptyMapper.map(throwable)

        // Then
        assertTrue(result is AppError.Unknown)
        assertEquals(throwable, (result as AppError.Unknown).throwable)
    }

    @Test
    fun `map should use custom plugin result before default mapping`() {
        // Given
        val plugin = mockk<ExceptionMapperPlugin>()
        val customError = AppError.Data.NotFound("hotel")
        every { plugin.map(any()) } returns customError
        val mapper = ExceptionMapper(setOf(plugin))

        // When
        val result = mapper.map(IOException("any"))

        // Then
        assertEquals(customError, result)
    }

    @Test
    fun `map should fall back to default mapping when plugin returns null`() {
        // Given
        val plugin = mockk<ExceptionMapperPlugin>()
        every { plugin.map(any()) } returns null
        val mapper = ExceptionMapper(setOf(plugin))

        // When
        val result = mapper.map(IOException("no network"))

        // Then
        assertEquals(AppError.Network.NoConnection, result)
    }
}