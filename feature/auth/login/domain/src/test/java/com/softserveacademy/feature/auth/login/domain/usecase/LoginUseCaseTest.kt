package com.softserveacademy.feature.auth.login.domain.usecase

import com.softserveacademy.feature.auth.login.domain.repository.LoginRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [LoginUseCase] following the project's testing policy.
 */
class LoginUseCaseTest {

    private val repository = mockk<LoginRepository>()
    private val useCase = LoginUseCase(repository)

    @Test
    fun `given valid credentials when login then returns success result`() = runTest {
        // GIVEN
        val email = "test@example.com"
        val password = "password123"
        coEvery { repository.login(email, password) } returns Result.success(Unit)

        // WHEN
        val result = useCase(email, password)

        // THEN
        assertTrue(result.isSuccess)
    }

    @Test
    fun `given empty email when login then returns failure result`() = runTest {
        // GIVEN
        val email = ""
        val password = "password123"

        // WHEN
        val result = useCase(email, password)

        // THEN
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }
}
