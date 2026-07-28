package com.softserveacademy.feature.auth.common.domain.usecase

import com.softserveacademy.feature.auth.common.domain.repository.SessionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [LogoutUseCase] following the project's testing policy.
 */
class LogoutUseCaseTest {

    private val repository = mockk<SessionRepository>()
    private val useCase = LogoutUseCase(repository)

    @Test
    fun `given session exists when logout then returns success result`() = runTest {
        // GIVEN
        coEvery { repository.logout() } returns Result.success(Unit)

        // WHEN
        val result = useCase()

        // THEN
        assertTrue(result.isSuccess)
    }
}
