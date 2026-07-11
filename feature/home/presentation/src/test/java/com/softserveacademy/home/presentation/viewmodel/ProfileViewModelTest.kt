package com.softserveacademy.home.presentation.viewmodel

import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.home.domain.usecases.GetProfileUseCase
import com.softserveacademy.home.presentation.state.ProfileState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given success when loadProfile then state is updated to Success`() = runTest {
        // GIVEN: The use case returns success
        val profile = UserProfile("John", 10, "url", "Location")
        coEvery { getProfileUseCase() } returns Result.success(profile)

        // WHEN: ViewModel is initialized (it calls loadProfile in init)
        val viewModel = ProfileViewModel(getProfileUseCase)

        // THEN: The state should be Success with the profile
        assertTrue(viewModel.state is ProfileState.Success)
        assertEquals(profile, (viewModel.state as ProfileState.Success).profile)
    }

    @Test
    fun `given error when loadProfile then state is updated to Error`() = runTest {
        // GIVEN: The use case returns failure
        coEvery { getProfileUseCase() } returns Result.failure(Exception("Error message"))

        // WHEN: ViewModel is initialized
        val viewModel = ProfileViewModel(getProfileUseCase)

        // THEN: The state should be Error with the message
        assertTrue(viewModel.state is ProfileState.Error)
        assertEquals("Error message", (viewModel.state as ProfileState.Error).message)
    }
}
