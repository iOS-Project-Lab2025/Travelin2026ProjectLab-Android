package com.softserveacademy.profile.domain.usecases

import com.softserveacademy.profile.domain.model.UserProfile
import com.softserveacademy.profile.domain.repository.ProfileRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateProfileUseCaseTest {

    private val repository = mockk<ProfileRepository>()
    private val useCase = UpdateProfileUseCase(repository)

    @Test
    fun `given valid profile when invoke then returns success`() = runTest {
        val profile = UserProfile("John", "Doe", 10, "url", location = "Santiago")
        coEvery { repository.updateProfile(profile, null) } returns Result.success(Unit)

        val result = useCase(profile)

        assertTrue(result.isSuccess)
        coVerify { repository.updateProfile(profile, null) }
    }

    @Test
    fun `given profile with password when invoke then password is passed`() = runTest {
        val profile = UserProfile("John", "Doe", 10, "url", location = "Santiago")
        coEvery { repository.updateProfile(profile, "newPass") } returns Result.success(Unit)

        val result = useCase(profile, "newPass")

        assertTrue(result.isSuccess)
        coVerify { repository.updateProfile(profile, "newPass") }
    }

    @Test
    fun `given error when invoke then returns failure`() = runTest {
        val profile = UserProfile("John", "Doe", 10, "url", location = "Santiago")
        coEvery { repository.updateProfile(profile, null) } returns Result.failure(Exception("Error"))

        val result = useCase(profile)

        assertTrue(result.isFailure)
        assertEquals("Error", result.exceptionOrNull()?.message)
    }
}
