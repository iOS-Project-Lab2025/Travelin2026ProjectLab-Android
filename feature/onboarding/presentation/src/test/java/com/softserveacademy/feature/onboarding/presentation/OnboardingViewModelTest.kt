package com.softserveacademy.feature.onboarding.presentation


import com.softserveacademy.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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
 * Unit tests for [OnboardingViewModel].
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
    @Test
    fun `when onGetStartedClick is called, it should execute use case and then navigate`() = runTest {
        // Given
        var callbackExecuted = false
        val onFinished = { callbackExecuted = true }

        // When
        viewModel.onGetStartedClick(onFinished)

        // Fast-forward coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Use case must be called and the navigation callback must be true
        coVerify(exactly = 1) { completeOnboardingUseCase() }
        assert(callbackExecuted)
    }
}