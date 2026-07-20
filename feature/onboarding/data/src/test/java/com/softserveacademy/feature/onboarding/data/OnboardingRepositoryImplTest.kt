package com.softserveacademy.feature.onboarding.data

import com.softserveacademy.core.domain.repository.CorePreferencesRepository
import com.softserveacademy.feature.onboarding.data.repository.OnboardingRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [OnboardingRepositoryImpl].
 * Verifies that the repository correctly interacts with CorePreferences.
 */
class OnboardingRepositoryImplTest {

    private lateinit var repository: OnboardingRepositoryImpl
    private val corePrefs: CorePreferencesRepository = mockk()

    @Before
    fun setup() {
        // Mock the suspend function to do nothing when called
        coEvery { corePrefs.setFirstTimeUser(any()) } returns Unit
        repository = OnboardingRepositoryImpl(corePrefs)
    }

    /**
     * Test case: Verifies that when [OnboardingRepository.completeOnboarding] is called,
     * the underlying preferences are updated to set "first time user" to false.
     */
    @Test
    fun `completeOnboarding should set first time user to false in preferences`() = runTest {
        // When
        repository.completeOnboarding()

        // Then: Verify that corePrefs was called with false
        coVerify(exactly = 1) { corePrefs.setFirstTimeUser(false) }
    }
}