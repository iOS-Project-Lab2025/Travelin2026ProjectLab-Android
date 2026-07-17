package com.softserveacademy.home.presentation.viewmodel

import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.home.domain.usecases.GetProfileUseCase
import com.softserveacademy.home.domain.usecases.UpdateProfileUseCase
import com.softserveacademy.home.presentation.state.EditProfileState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditProfileViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val getProfileUseCase = mockk<GetProfileUseCase>()
    private val updateProfileUseCase = mockk<UpdateProfileUseCase>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val mockProfile = UserProfile(
        firstName = "John",
        lastName = "Doe",
        points = 100,
        avatarUrl = "url",
        phone = "+855 123456",
        age = 30,
        location = "Mars"
    )

    @Test
    fun `given success when loadProfile then state is updated and fields are prefilled`() = runTest {
        coEvery { getProfileUseCase() } returns Result.success(mockProfile)

        val viewModel = EditProfileViewModel(getProfileUseCase, updateProfileUseCase)

        assertTrue(viewModel.state is EditProfileState.Success)
        assertEquals("John", viewModel.firstName)
        assertEquals("Doe", viewModel.lastName)
        assertEquals("+855", viewModel.countryCode)
        assertEquals("123456", viewModel.phone)
        assertEquals("30", viewModel.age)
        assertEquals("Mars", viewModel.location)
    }

    @Test
    fun `given no changes when hasChanges called then returns false`() = runTest {
        coEvery { getProfileUseCase() } returns Result.success(mockProfile)
        val viewModel = EditProfileViewModel(getProfileUseCase, updateProfileUseCase)

        assertFalse(viewModel.hasChanges())
    }

    @Test
    fun `given changes when hasChanges called then returns true`() = runTest {
        coEvery { getProfileUseCase() } returns Result.success(mockProfile)
        val viewModel = EditProfileViewModel(getProfileUseCase, updateProfileUseCase)

        viewModel.firstName = "Jane"
        assertTrue(viewModel.hasChanges())
    }

    @Test
    fun `given location change when hasChanges called then returns true`() = runTest {
        coEvery { getProfileUseCase() } returns Result.success(mockProfile)
        val viewModel = EditProfileViewModel(getProfileUseCase, updateProfileUseCase)

        viewModel.location = "Earth"
        assertTrue(viewModel.hasChanges())
    }

    @Test
    fun `given passwords mismatch when onSaveChanges then state is Error`() = runTest {
        coEvery { getProfileUseCase() } returns Result.success(mockProfile)
        val viewModel = EditProfileViewModel(getProfileUseCase, updateProfileUseCase)

        viewModel.password = "pass1"
        viewModel.confirmPassword = "pass2"
        viewModel.onSaveChanges()

        assertTrue(viewModel.state is EditProfileState.Error)
        assertEquals("Passwords do not match", (viewModel.state as EditProfileState.Error).message)
    }

    @Test
    fun `given valid changes when onSaveChanges then updateProfileUseCase is called and state is UpdateSuccess`() = runTest {
        coEvery { getProfileUseCase() } returns Result.success(mockProfile)
        coEvery { updateProfileUseCase(any(), any()) } returns Result.success(Unit)
        val viewModel = EditProfileViewModel(getProfileUseCase, updateProfileUseCase)

        viewModel.firstName = "Jane"
        viewModel.onSaveChanges()

        assertTrue(viewModel.state is EditProfileState.UpdateSuccess)
    }
}
