package com.softserveacademy.profile.presentation.viewmodel

import com.softserveacademy.core.domain.model.AppTheme
import com.softserveacademy.core.domain.usecase.GetThemeUseCase
import com.softserveacademy.core.domain.usecase.SetThemeUseCase
import com.softserveacademy.feature.auth.common.domain.usecase.LogoutUseCase
import com.softserveacademy.profile.domain.model.UserProfile
import com.softserveacademy.profile.domain.usecases.GetProfileUseCase
import com.softserveacademy.profile.presentation.state.ProfileState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {
    private val getProfileUseCase = mockk<GetProfileUseCase>()
    private val logoutUseCase = mockk<LogoutUseCase>()
    private val getThemeUseCase = mockk<GetThemeUseCase>()
    private val setThemeUseCase = mockk<SetThemeUseCase>()
    
    private lateinit var viewModel: ProfileViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    private val profile = UserProfile("John", "Doe", 10, "url", location = "Location")

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getThemeUseCase() } returns flowOf(AppTheme.SYSTEM)
        coEvery { getProfileUseCase() } returns Result.success(profile)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when init then loads profile and theme`() = runTest {
        viewModel = ProfileViewModel(getProfileUseCase, logoutUseCase, getThemeUseCase, setThemeUseCase)
        
        assertEquals(ProfileState.Success(profile), viewModel.state)
        assertEquals(AppTheme.SYSTEM, viewModel.currentTheme)
    }

    @Test
    fun `when loadProfile fails then state is Error`() = runTest {
        coEvery { getProfileUseCase() } returns Result.failure(Exception("Error"))
        viewModel = ProfileViewModel(getProfileUseCase, logoutUseCase, getThemeUseCase, setThemeUseCase)
        
        assertTrue(viewModel.state is ProfileState.Error)
    }

    @Test
    fun `when onLogoutClick then calls logoutUseCase`() = runTest {
        coEvery { logoutUseCase() } returns Result.success(Unit)
        viewModel = ProfileViewModel(getProfileUseCase, logoutUseCase, getThemeUseCase, setThemeUseCase)
        
        viewModel.onLogoutClick()
        
        coVerify { logoutUseCase() }
    }
}
