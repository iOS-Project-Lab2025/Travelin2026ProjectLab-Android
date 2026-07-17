package com.softserveacademy.home.domain.usecases

import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.home.domain.repository.ProfileRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [GetProfileUseCase] following the project's testing policy.
 */
class GetProfileUseCaseTest {

    private val repository = mockk<ProfileRepository>()
    private val useCase = GetProfileUseCase(repository)

    @Test
    fun `given valid profile when invoke then returns success with profile`() = runTest {
        val profile = UserProfile("John", "Doe", 10, "url", location = "Location")
        coEvery { repository.getProfile() } returns Result.success(profile)

        // WHEN: The use case is invoked
        val result = useCase()

        // THEN: The result should be success and contain the profile
        assertTrue(result.isSuccess)
        assertEquals(profile, result.getOrNull())
    }

    @Test
    fun `given repository error when invoke then returns failure`() = runTest {
        // GIVEN: The repository returns an error
        coEvery { repository.getProfile() } returns Result.failure(Exception("Network error"))

        // WHEN: The use case is invoked
        val result = useCase()

        // THEN: The result should be failure
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}
