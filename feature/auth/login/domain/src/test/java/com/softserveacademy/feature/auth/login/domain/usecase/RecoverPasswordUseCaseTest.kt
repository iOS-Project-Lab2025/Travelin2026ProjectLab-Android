package com.softserveacademy.feature.auth.login.domain.usecase

import com.softserveacademy.feature.auth.login.domain.repository.LoginRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [RecoverPasswordUseCase] following the project's testing policy.
 */
class RecoverPasswordUseCaseTest {

    private val repository = mockk<LoginRepository>()
    private val useCase = RecoverPasswordUseCase(repository)

    @Test
    fun `given valid email when recover password then returns success result`() = runTest {
        // GIVEN
        val email = "test@example.com"
        coEvery { repository.recoverPassword(email) } returns Result.success(Unit)

        // WHEN
        val result = useCase(email)

        // THEN
        assertTrue(result.isSuccess)
    }

    @Test
    fun `given invalid email when recover password then returns failure result`() = runTest {
        // GIVEN
        val email = "invalid-email"

        // WHEN
        val result = useCase(email)

        // THEN
        assertTrue(result.isFailure)
    }
}
