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
import java.util.Calendar

class UpdateProfileUseCaseTest {

    private val repository = mockk<ProfileRepository>()
    private val useCase = UpdateProfileUseCase(repository)

    private fun getBirthDate(age: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -age)
        return calendar.timeInMillis
    }

    @Test
    fun `given valid profile when invoke then returns success`() = runTest {
        val profile = UserProfile("John", "Doe", 10, "url", birthDate = getBirthDate(25), location = "Santiago")
        coEvery { repository.updateProfile(profile, null) } returns Result.success(Unit)

        val result = useCase(profile)

        assertTrue(result.isSuccess)
        coVerify { repository.updateProfile(profile, null) }
    }

    @Test
    fun `given profile with password when invoke then password is passed`() = runTest {
        val profile = UserProfile("John", "Doe", 10, "url", birthDate = getBirthDate(25), location = "Santiago")
        coEvery { repository.updateProfile(profile, "newPass") } returns Result.success(Unit)

        val result = useCase(profile, "newPass")

        assertTrue(result.isSuccess)
        coVerify { repository.updateProfile(profile, "newPass") }
    }

    @Test
    fun `given error when invoke then returns failure`() = runTest {
        val profile = UserProfile("John", "Doe", 10, "url", birthDate = getBirthDate(25), location = "Santiago")
        coEvery { repository.updateProfile(profile, null) } returns Result.failure(Exception("Error"))

        val result = useCase(profile)

        assertTrue(result.isFailure)
        assertEquals("Error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `given profile with age under 18 when invoke then returns failure`() = runTest {
        val profile = UserProfile("John", "Doe", 10, "url", birthDate = getBirthDate(17), location = "Santiago")

        val result = useCase(profile)

        assertTrue(result.isFailure)
        assertEquals("You must be at least 18 years old", result.exceptionOrNull()?.message)
    }

    @Test
    fun `given short password when invoke then returns failure`() = runTest {
        val profile = UserProfile("John", "Doe", 10, "url", birthDate = getBirthDate(25), location = "Santiago")

        val result = useCase(profile, "123")

        assertTrue(result.isFailure)
        assertEquals("Password must be at least 6 characters long", result.exceptionOrNull()?.message)
    }
}
