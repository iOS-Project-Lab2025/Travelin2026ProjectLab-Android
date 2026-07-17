package com.softserveacademy.home.presentation.viewmodel

import com.softserveacademy.core.domain.model.AppTheme
import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.core.domain.usecase.GetThemeUseCase
import com.softserveacademy.core.domain.usecase.SetThemeUseCase
import com.softserveacademy.feature.auth.common.domain.usecase.LogoutUseCase
import com.softserveacademy.home.domain.usecases.GetProfileUseCase
import com.softserveacademy.home.presentation.state.ProfileState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [ProfileViewModel] following the project's testing policy.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val getProfileUseCase = mockk<GetProfileUseCase>()
    private val logoutUseCase = mockk<LogoutUseCase>()
    private val getThemeUseCase = mockk<GetThemeUseCase>()
    private val setThemeUseCase = mockk<SetThemeUseCase>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getThemeUseCase() } returns flowOf(AppTheme.SYSTEM)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given success when loadProfile then state is updated to Success`() = runTest {
        // GIVEN: The use case returns success
        val profile = UserProfile("John", "Doe", 10, "url", location = "Location")
        coEvery { getProfileUseCase() } returns Result.success(profile)

        // WHEN: ViewModel is initialized (it calls loadProfile in init)
        val viewModel = ProfileViewModel(getProfileUseCase, logoutUseCase, getThemeUseCase, setThemeUseCase)

        // THEN: The state should be Success with the profile
        assertTrue(viewModel.state is ProfileState.Success)
        assertEquals(profile, (viewModel.state as ProfileState.Success).profile)
    }

    @Test
    fun `given error when loadProfile then state is updated to Error`() = runTest {
        // GIVEN: The use case returns failure
        coEvery { getProfileUseCase() } returns Result.failure(Exception("Error message"))

        // WHEN: ViewModel is initialized
        val viewModel = ProfileViewModel(getProfileUseCase, logoutUseCase, getThemeUseCase, setThemeUseCase)

        // THEN: The state should be Error with the message
        assertTrue(viewModel.state is ProfileState.Error)
        assertEquals("Error message", (viewModel.state as ProfileState.Error).message)
    }

    @Test
    fun `when onLogoutClick then logoutUseCase is called and logoutEvent is emitted`() = runTest {
        // GIVEN: Logout is successful
        coEvery { getProfileUseCase() } returns Result.success(mockk())
        coEvery { logoutUseCase() } returns Result.success(Unit)
        val viewModel = ProfileViewModel(getProfileUseCase, logoutUseCase, getThemeUseCase, setThemeUseCase)

        // WHEN: logout is clicked
        var eventEmitted = false
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.logoutEvent.collect {
                eventEmitted = true
            }
        }
        viewModel.onLogoutClick()

        // THEN: logoutUseCase is called and event is emitted
        coVerify { logoutUseCase() }
        assertTrue(eventEmitted)
        job.cancel()
    }
}
