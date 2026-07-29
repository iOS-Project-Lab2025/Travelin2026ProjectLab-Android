package com.softserveacademycore.presentation.ui.splash

import com.softserveacademy.core.domain.model.splash.SplashDestination
import com.softserveacademy.core.domain.usecase.splash.GetSplashDestinationUseCase
import com.softserveacademycore.presentation.ui.splash.events.SplashEvent
import com.softserveacademycore.presentation.ui.splash.viewmodels.SplashViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private val useCase = mockk<GetSplashDestinationUseCase>()
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
        coEvery { useCase() } returns SplashDestination.Onboarding

        // WHEN
        viewModel = SplashViewModel(useCase)
        viewModel.onEvent(SplashEvent.OnViewReady)

        advanceTimeBy(3001.milliseconds) // Jump past the 5s delay

        // THEN
        assertEquals(SplashDestination.Onboarding, viewModel.uiState.value.destination)
    }

    @Test
    fun `given returning user when delay finishes then destination is Home`() = runTest {
        // GIVEN
        coEvery { useCase() } returns SplashDestination.Login

        // WHEN
        viewModel = SplashViewModel(useCase)
        viewModel.onEvent(SplashEvent.OnViewReady)
        advanceTimeBy(3001.milliseconds)

        // THEN
        assertEquals(SplashDestination.Login, viewModel.uiState.value.destination)
    }
}