package com.softserveacademy.home.domain.usecases

import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.home.domain.repository.HomeRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetUserProfileUseCaseTest {

    private val repository = mockk<HomeRepository>()
    private val useCase = GetUserProfileUseCase(repository)

    @Test
    fun `given success when invoke then returns profile`() = runTest {
        val profile = UserProfile("John", "Doe", 10, "url", location = "Santiago")
        coEvery { repository.getUserProfile() } returns Result.success(profile)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(profile, result.getOrNull())
    }

    @Test
    fun `given error when invoke then returns failure`() = runTest {
        coEvery { repository.getUserProfile() } returns Result.failure(Exception("Error"))

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals("Error", result.exceptionOrNull()?.message)
    }
}
