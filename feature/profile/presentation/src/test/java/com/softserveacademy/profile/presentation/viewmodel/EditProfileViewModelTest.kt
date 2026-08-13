package com.softserveacademy.profile.presentation.viewmodel

import com.softserveacademy.profile.domain.model.UserProfile
import com.softserveacademy.profile.domain.usecases.GetProfileUseCase
import com.softserveacademy.profile.domain.usecases.UpdateProfileUseCase
import com.softserveacademy.profile.presentation.state.EditProfileState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditProfileViewModelTest {
    private val getProfileUseCase = mockk<GetProfileUseCase>()
    private val updateProfileUseCase = mockk<UpdateProfileUseCase>()
    
    private lateinit var viewModel: EditProfileViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    private val profile = UserProfile(
        firstName = "John",
        lastName = "Doe",
        points = 100,
        avatarUrl = "url",
        phone = "+855 123456",
        birthDate = 839808000000L,
        location = "Mars"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getProfileUseCase() } returns Result.success(profile)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when init then loads profile fields`() = runTest {
        viewModel = EditProfileViewModel(getProfileUseCase, updateProfileUseCase)
        
        assertEquals("John", viewModel.firstName)
        assertEquals("Doe", viewModel.lastName)
        assertEquals("+855", viewModel.countryCode)
        assertEquals("123456", viewModel.phone)
        assertEquals(839808000000L, viewModel.birthDate)
        assertEquals("Mars", viewModel.location)
    }

    @Test
    fun `when onSaveChanges success then state is UpdateSuccess`() = runTest {
        viewModel = EditProfileViewModel(getProfileUseCase, updateProfileUseCase)
        coEvery { updateProfileUseCase(any(), any()) } returns Result.success(Unit)
        
        viewModel.firstName = "NewName"
        viewModel.onSaveChanges()
        
        assertEquals(EditProfileState.UpdateSuccess, viewModel.state)
    }
}
