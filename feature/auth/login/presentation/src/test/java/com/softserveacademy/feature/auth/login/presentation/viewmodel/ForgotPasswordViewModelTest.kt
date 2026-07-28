package com.softserveacademy.feature.auth.login.presentation.viewmodel

import com.softserveacademy.feature.auth.login.domain.usecase.RecoverPasswordUseCase
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
 * Unit tests for [ForgotPasswordViewModel] following the project's testing policy.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ForgotPasswordViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val recoverPasswordUseCase = mockk<RecoverPasswordUseCase>()
    private lateinit var viewModel: ForgotPasswordViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ForgotPasswordViewModel(recoverPasswordUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given valid email when recover clicked then sets success state`() = runTest {
        // GIVEN
        viewModel.email = "test@example.com"
        coEvery { recoverPasswordUseCase.invoke(any()) } returns Result.success(Unit)

        // WHEN
        viewModel.onRecoverClick()

        // THEN
        assertTrue(viewModel.isSuccess)
    }

    @Test
    fun `given service error when recover clicked then sets error message`() = runTest {
        // GIVEN
        viewModel.email = "test@example.com"
        coEvery { recoverPasswordUseCase.invoke(any()) } returns Result.failure(Exception("Service error"))

        // WHEN
        viewModel.onRecoverClick()

        // THEN
        assertEquals("Service error", viewModel.error)
    }
}
