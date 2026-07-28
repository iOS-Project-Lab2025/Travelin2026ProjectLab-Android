package com.softserveacademy.feature.auth.login.presentation.viewmodel

import com.softserveacademy.feature.auth.login.domain.usecase.LoginUseCase
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
 * Unit tests for [LoginViewModel] following the project's testing policy.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val loginUseCase = mockk<LoginUseCase>()
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(loginUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given valid credentials when login clicked then sets success state`() = runTest {
        // GIVEN
        viewModel.email = "test@example.com"
        viewModel.password = "password"
        coEvery { loginUseCase.invoke(any(), any()) } returns Result.success(Unit)

        // WHEN
        viewModel.onLoginClick()

        // THEN
        assertTrue(viewModel.isSuccess)
    }

    @Test
    fun `given service error when login clicked then sets error message`() = runTest {
        // GIVEN
        viewModel.email = "test@example.com"
        viewModel.password = "password"
        coEvery { loginUseCase.invoke(any(), any()) } returns Result.failure(Exception("Service error"))

        // WHEN
        viewModel.onLoginClick()

        // THEN
        assertEquals("Service error", viewModel.error)
    }
}
