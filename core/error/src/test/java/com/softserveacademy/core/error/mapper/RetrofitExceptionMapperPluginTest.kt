package com.softserveacademy.core.error.mapper

import com.softserveacademy.core.error.model.AppError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.HttpException

class RetrofitExceptionMapperPluginTest {

    private val plugin = RetrofitExceptionMapperPlugin()

    @Test
    fun `map should return session expired for http 401`() {
        // When
        val result = plugin.map(HttpException(401, "Unauthorized"))

        // Then
        assertEquals(AppError.Auth.SessionExpired, result)
    }

    @Test
    fun `map should return unauthorized for http 403`() {
        // When
        val result = plugin.map(HttpException(403, "Forbidden"))

        // Then
        assertEquals(AppError.Auth.Unauthorized, result)
    }

    @Test
    fun `map should return not found for http 404`() {
        // When
        val result = plugin.map(HttpException(404, "Not found"))

        // Then
        assertEquals(AppError.Data.NotFound("resource"), result)
    }

    @Test
    fun `map should return server error for http 5xx`() {
        // When
        val result = plugin.map(HttpException(500, "Internal error"))

        // Then
        assertEquals(AppError.Network.Server(500, "Internal error"), result)
    }

    @Test
    fun `map should return server error for other http codes`() {
        // When
        val result = plugin.map(HttpException(400, "Bad request"))

        // Then
        assertEquals(AppError.Network.Server(400, "Bad request"), result)
    }

    @Test
    fun `map should return null when throwable is not an http exception`() {
        // When
        val result = plugin.map(IllegalArgumentException("not http"))

        // Then
        assertNull(result)
    }
}