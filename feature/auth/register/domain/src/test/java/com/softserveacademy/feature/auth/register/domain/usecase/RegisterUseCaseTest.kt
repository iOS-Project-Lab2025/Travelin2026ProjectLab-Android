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
        val user = User("First", "Last", "test@example.com", 25, "123456")
        val password = "password123"
        coEvery { repository.register(user, password) } returns Result.success(Unit)

        // WHEN
        val result = useCase(user, password)

        // THEN
        assertTrue(result.isSuccess)
    }

    @Test
    fun `given empty email when register then returns failure result`() = runTest {
        // GIVEN
        val user = User("First", "Last", "", 25, "123456")
        val password = "password123"

        // WHEN
        val result = useCase(user, password)

        // THEN
        assertTrue(result.isFailure)
    }
}
