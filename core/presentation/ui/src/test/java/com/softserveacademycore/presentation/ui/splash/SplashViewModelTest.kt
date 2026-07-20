package com.softserveacademycore.presentation.ui.splash

import com.softserveacademy.core.domain.repository.CorePreferencesRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

/**
 * Unit tests for SplashViewModel logic.
 * Verifies that the state correctly reflects navigation decisions after the delay.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private val repository = mockk<CorePreferencesRepository>()
    private lateinit var viewModel: SplashViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given first time user when delay finishes then destination is Onboarding`() = runTest {
        // GIVEN
        every { repository.isFirstTimeUser() } returns flowOf(true)

        // WHEN
        viewModel = SplashViewModel(repository)
        advanceTimeBy(5001.milliseconds) // Jump past the 5s delay

        // THEN
        assertEquals(SplashDestination.Onboarding, viewModel.destination.value)
    }

    @Test
    fun `given returning user when delay finishes then destination is Home`() = runTest {
        // GIVEN
        every { repository.isFirstTimeUser() } returns flowOf(false)

        // WHEN
        viewModel = SplashViewModel(repository)
        advanceTimeBy(5001.milliseconds)

        // THEN
        assertEquals(SplashDestination.Login, viewModel.destination.value)
    }
}