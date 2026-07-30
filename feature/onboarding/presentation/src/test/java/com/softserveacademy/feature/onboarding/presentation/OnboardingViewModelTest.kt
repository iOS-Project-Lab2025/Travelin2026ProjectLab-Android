package com.softserveacademy.feature.onboarding.presentation


import com.softserveacademy.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.softserveacademy.feature.onboarding.presentation.events.OnboardingEvent
import com.softserveacademy.feature.onboarding.presentation.viewmodels.OnboardingViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [com.softserveacademy.feature.onboarding.presentation.viewmodels.OnboardingViewModel].
 * This class tests the presentation logic of the onboarding flow.
 * It uses a [StandardTestDispatcher] to control the execution of coroutines
 * and verify that operations happen in the correct order.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {

    private lateinit var viewModel: OnboardingViewModel
    private val completeOnboardingUseCase: CompleteOnboardingUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Set the Main dispatcher to our test dispatcher for Coroutines
        Dispatchers.setMain(testDispatcher)
        coEvery { completeOnboardingUseCase() } returns Unit
        viewModel = OnboardingViewModel(completeOnboardingUseCase)
    }

    @After
    fun tearDown() {
        // Reset the dispatcher after the test
        Dispatchers.resetMain()
    }

    /**
     * Test case: Ensures that the "Get Started" flow executes the use case
     * and only then triggers the navigation to the next screen.
     */
    /**
     * Verifies that when the [OnboardingEvent.OnGetStartedClick] event is processed,
     * the [CompleteOnboardingUseCase] is executed and the UI state is updated
     * to reflect that onboarding is completed.
     */
    @Test
    fun `when OnGetStartedClick event is processed, then state updates to completed`() = runTest {
        // When: The user interacts with the "Get Started" action
        viewModel.onEvent(OnboardingEvent.OnGetStartedClick)

        // Fast-forward coroutines to ensure all state updates are processed
        testDispatcher.scheduler.advanceUntilIdle()

        // Then:
        // 1. Verify business logic was triggered exactly once
        coVerify(exactly = 1) { completeOnboardingUseCase() }

        // 2. Verify the state transition matches the expected outcome
        assertTrue("State should be marked as completed", viewModel.uiState.value.isCompleted)
    }
}