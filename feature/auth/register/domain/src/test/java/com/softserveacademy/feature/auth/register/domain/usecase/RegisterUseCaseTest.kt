package com.softserveacademy.feature.auth.register.domain.usecase

import com.softserveacademy.core.domain.model.User
import com.softserveacademy.feature.auth.register.domain.repository.RegisterRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [RegisterUseCase] following the project's testing policy.
 */
class RegisterUseCaseTest {

    private val repository = mockk<RegisterRepository>()
    private val useCase = RegisterUseCase(repository)

    @Test
    fun `given valid user and password when register then returns success result`() = runTest {
        // GIVEN
        val user = User("First", "Last", "+855 123456", 25, "test@example.com")
        val password = "password123"
        coEvery { repository.register(user, password) } returns Result.success(Unit)

        // WHEN
        val result = useCase(user, password)

        // THEN
        assertTrue(result.isSuccess)
    }

    @Test
    fun `given user younger than 18 when register then returns failure result`() = runTest {
        // GIVEN
        val user = User("First", "Last", "+855 123456", 17, "test@example.com")
        val password = "password123"

        // WHEN
        val result = useCase(user, password)

        // THEN
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message == "You must be at least 18 years old to register")
    }

    @Test
    fun `given invalid email format when register then returns failure result`() = runTest {
        // GIVEN
        val user = User("First", "Last", "+855 123456", 25, "invalid-email")
        val password = "password123"

        // WHEN
        val result = useCase(user, password)

        // THEN
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message == "Invalid email format")
    }

    @Test
    fun `given short password when register then returns failure result`() = runTest {
        // GIVEN
        val user = User("First", "Last", "+855 123456", 25, "test@example.com")
        val password = "123"

        // WHEN
        val result = useCase(user, password)

        // THEN
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message == "Password must be at least 6 characters long")
    }

    @Test
    fun `given empty first name when register then returns failure result`() = runTest {
        // GIVEN
        val user = User("", "Last", "+855 123456", 25, "test@example.com")
        val password = "password123"

        // WHEN
        val result = useCase(user, password)

        // THEN
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message == "First name cannot be empty")
    }
}
